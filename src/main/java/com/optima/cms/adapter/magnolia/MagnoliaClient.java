package com.optima.cms.adapter.magnolia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaPlan;
import com.optima.cms.exception.MagnoliaUnavailableException;
import com.optima.cms.exception.MagnoliaUpstreamException;
import com.optima.cms.model.plan.FindAllRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

import static com.optima.cms.adapter.magnolia.common.MagnoliaConstants.MOCK_PLANS_JSON;

@Component
@Slf4j
public class MagnoliaClient {

    /** Built in {@link com.optima.cms.config.MagnoliaClientConfig#magnoliaWebClient()} (base URL + basic auth). */
    private final WebClient magnoliaWebClient;
    private final ObjectMapper objectMapper;
    private final MagnoliaRestPaths magnoliaRestPaths;

    public MagnoliaClient(
            @Qualifier("magnoliaWebClient") WebClient magnoliaWebClient,
            ObjectMapper objectMapper,
            MagnoliaRestPaths magnoliaRestPaths) {
        this.magnoliaWebClient = magnoliaWebClient;
        this.objectMapper = objectMapper;
        this.magnoliaRestPaths = magnoliaRestPaths;
    }

    /**
     * GET plan catalog from Magnolia delivery ({@link MagnoliaRestPaths#deliveryPlanCatalog()}).
     * The {@link FindAllRequest} is accepted for API symmetry; filtering is done in {@link MagnoliaPlanCatalogAdapter}.
     */
    public List<MagnoliaPlan> getPlans(FindAllRequest request) {
        String path = magnoliaRestPaths.deliveryPlanCatalog();
        String raw;
        try {
            raw = magnoliaWebClient.get()
                    .uri(path)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw MagnoliaUpstreamException.from(e, "delivery plan catalog " + path);
        } catch (RuntimeException e) {
            if (MagnoliaConnectionFailures.isConnectionFailure(e)) {
                throw new MagnoliaUnavailableException("Cannot connect to Magnolia at " + path, e);
            }
            throw e;
        }
        if (raw == null) {
            log.info("Magnolia delivery planSchema response: <null body>");
            return List.of();
        }
        if (raw.isBlank()) {
            log.warn("Magnolia delivery planSchema response: <empty body>");
            return List.of();
        }
        if (log.isDebugEnabled()) {
            log.debug("Magnolia delivery planSchema raw JSON ({} chars): {}", raw.length(), raw);
        }
        try {
            return parsePlanDeliveryPayload(raw);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Magnolia delivery response is not valid JSON or plan shape", e);
        }
    }

	/**
	 * Parses delivery JSON: either a top-level array, or an object with a {@code docs} / {@code results} array.
	 */
	private List<MagnoliaPlan> parsePlanDeliveryPayload(String response) throws JsonProcessingException {
		JsonNode root = objectMapper.readTree(response);
		JsonNode arrayNode = null;
		if (root.isArray()) {
			arrayNode = root;
		} else if (root.isObject()) {
			if (root.hasNonNull("docs") && root.get("docs").isArray()) {
				arrayNode = root.get("docs");
			} else if (root.hasNonNull("results") && root.get("results").isArray()) {
				arrayNode = root.get("results");
			}
		}
		if (arrayNode == null || !arrayNode.isArray()) {
			log.warn("Magnolia delivery payload has no array at root, under 'docs', or under 'results'; returning empty list");
			return List.of();
		}
		List<MagnoliaPlan> out = new ArrayList<>();
		for (JsonNode n : arrayNode) {
			if (n == null || n.isNull()) {
				continue;
			}
			out.add(objectMapper.treeToValue(n, MagnoliaPlan.class));
		}
		return out;
	}

    public List<MagnoliaPlan> getPlans2(FindAllRequest request) {
        String path = magnoliaRestPaths.nodesPlanProbe();
        try {
            magnoliaWebClient.get()
                    .uri(path)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw MagnoliaUpstreamException.from(e, "nodes plan probe " + path);
        } catch (RuntimeException e) {
            if (MagnoliaConnectionFailures.isConnectionFailure(e)) {
                throw new MagnoliaUnavailableException("Cannot connect to Magnolia at " + path, e);
            }
            throw e;
        }
        log.info("Magnolia nodes probe completed; using classpath mock catalog body");
        try {
            return objectMapper.readValue(MOCK_PLANS_JSON, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Mock catalog JSON is invalid", e);
        }
    }
}
