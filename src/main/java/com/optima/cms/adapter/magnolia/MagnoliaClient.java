package com.optima.cms.adapter.magnolia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.optima.cms.adapter.magnolia.dto.content.MagnoliaContentCard;
import com.optima.cms.adapter.magnolia.dto.device.MagnoliaDevice;
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
	 * GET device catalog from Magnolia delivery ({@link MagnoliaRestPaths#deliveryDeviceCatalog()}).
	 * Filtering is applied in {@link MagnoliaDeviceCatalogAdapter}.
	 */
	public List<MagnoliaDevice> getDevices(FindAllRequest request) {
		String path = magnoliaRestPaths.deliveryDeviceCatalog();
		String raw;
		try {
			raw = magnoliaWebClient.get()
					.uri(path)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (WebClientResponseException e) {
			throw MagnoliaUpstreamException.from(e, "delivery device catalog " + path);
		} catch (RuntimeException e) {
			if (MagnoliaConnectionFailures.isConnectionFailure(e)) {
				throw new MagnoliaUnavailableException("Cannot connect to Magnolia at " + path, e);
			}
			throw e;
		}
		if (raw == null) {
			log.info("Magnolia delivery device response: <null body>");
			return List.of();
		}
		if (raw.isBlank()) {
			log.warn("Magnolia delivery device response: <empty body>");
			return List.of();
		}
		if (!log.isDebugEnabled()) {
			log.info("Magnolia delivery device raw JSON ({} chars): {}", raw.length(), raw);
		}
		try {
			return parseDeviceDeliveryPayload(raw);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Magnolia delivery response is not valid JSON or device shape", e);
		}
	}

	/**
	 * GET content-card catalog from Magnolia delivery ({@link MagnoliaRestPaths#deliveryContentCardCatalog()}).
	 * Filtering by UI page is done in {@link MagnoliaContentCardCatalogAdapter}.
	 */
	public List<MagnoliaContentCard> getContentCards() {
		String path = magnoliaRestPaths.deliveryContentCardCatalog();
		String raw;
		try {
			raw = magnoliaWebClient.get()
					.uri(path)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (WebClientResponseException e) {
			throw MagnoliaUpstreamException.from(e, "delivery content-card catalog " + path);
		} catch (RuntimeException e) {
			if (MagnoliaConnectionFailures.isConnectionFailure(e)) {
				throw new MagnoliaUnavailableException("Cannot connect to Magnolia at " + path, e);
			}
			throw e;
		}
		if (raw == null) {
			log.info("Magnolia delivery content-card response: <null body>");
			return List.of();
		}
		if (raw.isBlank()) {
			log.warn("Magnolia delivery content-card response: <empty body>");
			return List.of();
		}
		if (log.isDebugEnabled()) {
			log.debug("Magnolia delivery content-card raw JSON ({} chars): {}", raw.length(), raw);
		}
		try {
			return parseContentCardDeliveryPayload(raw);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Magnolia delivery response is not valid JSON or content-card shape", e);
		}
	}

	/**
	 * GET header delivery ({@link MagnoliaRestPaths#deliveryHeaderCatalog()}): envelope with {@code total}, {@code offset},
	 * {@code limit}, and JCR-style {@code results}. Parsed by {@link com.optima.cms.adapter.magnolia.header.MagnoliaHeaderDeliveryParser}.
	 */
	public JsonNode getHeaderDeliveryEnvelope() {
		String path = magnoliaRestPaths.deliveryHeaderCatalog();
		String raw;
		try {
			raw = magnoliaWebClient.get()
					.uri(path)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (WebClientResponseException e) {
			throw MagnoliaUpstreamException.from(e, "delivery header catalog " + path);
		} catch (RuntimeException e) {
			if (MagnoliaConnectionFailures.isConnectionFailure(e)) {
				throw new MagnoliaUnavailableException("Cannot connect to Magnolia at " + path, e);
			}
			throw e;
		}
		if (raw == null || raw.isBlank()) {
			return objectMapper.getNodeFactory().objectNode();
		}
		if (log.isDebugEnabled()) {
			log.debug("Magnolia delivery header raw JSON ({} chars)", raw.length());
		}
		try {
			return objectMapper.readTree(raw);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Magnolia header delivery is not valid JSON", e);
		}
	}

	/**
	 * GET footer delivery ({@link MagnoliaRestPaths#deliveryFooterCatalog()}): envelope with {@code total}, {@code offset},
	 * {@code limit}, and JCR-style {@code results}. Parsed by {@link com.optima.cms.adapter.magnolia.footer.MagnoliaFooterDeliveryParser}.
	 */
	public JsonNode getFooterDeliveryEnvelope() {
		String path = magnoliaRestPaths.deliveryFooterCatalog();
		String raw;
		try {
			raw = magnoliaWebClient.get()
					.uri(path)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (WebClientResponseException e) {
			throw MagnoliaUpstreamException.from(e, "delivery footer catalog " + path);
		} catch (RuntimeException e) {
			if (MagnoliaConnectionFailures.isConnectionFailure(e)) {
				throw new MagnoliaUnavailableException("Cannot connect to Magnolia at " + path, e);
			}
			throw e;
		}
		if (raw == null || raw.isBlank()) {
			return objectMapper.getNodeFactory().objectNode();
		}
		if (log.isDebugEnabled()) {
			log.debug("Magnolia delivery footer raw JSON ({} chars)", raw.length());
		}
		try {
			return objectMapper.readTree(raw);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Magnolia footer delivery is not valid JSON", e);
		}
	}

	/**
	 * GET theme body delivery ({@link MagnoliaRestPaths#deliveryThemeCatalog()}): envelope with {@code total}, {@code offset},
	 * {@code limit}, and JCR-style {@code results}. Parsed by {@link com.optima.cms.adapter.magnolia.theme.MagnoliaThemeDeliveryParser}.
	 */
	public JsonNode getThemeDeliveryEnvelope() {
		String path = magnoliaRestPaths.deliveryThemeCatalog();
		String raw;
		try {
			raw = magnoliaWebClient.get()
					.uri(path)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (WebClientResponseException e) {
			throw MagnoliaUpstreamException.from(e, "delivery theme catalog " + path);
		} catch (RuntimeException e) {
			if (MagnoliaConnectionFailures.isConnectionFailure(e)) {
				throw new MagnoliaUnavailableException("Cannot connect to Magnolia at " + path, e);
			}
			throw e;
		}
		if (raw == null || raw.isBlank()) {
			return objectMapper.getNodeFactory().objectNode();
		}
		if (log.isDebugEnabled()) {
			log.debug("Magnolia delivery theme raw JSON ({} chars)", raw.length());
		}
		try {
			return objectMapper.readTree(raw);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Magnolia theme delivery is not valid JSON", e);
		}
	}

	/**
	 * Parses delivery JSON: either a top-level array, or an object with a {@code docs} / {@code results} array.
	 */
	private List<MagnoliaPlan> parsePlanDeliveryPayload(String response) throws JsonProcessingException {
		JsonNode arrayNode = magnoliaDeliveryArray(response);
		if (arrayNode == null) {
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

	private List<MagnoliaDevice> parseDeviceDeliveryPayload(String response) throws JsonProcessingException {
		JsonNode arrayNode = magnoliaDeliveryArray(response);
		if (arrayNode == null) {
			return List.of();
		}
		List<MagnoliaDevice> out = new ArrayList<>();
		for (JsonNode n : arrayNode) {
			if (n == null || n.isNull()) {
				continue;
			}
			out.add(objectMapper.treeToValue(n, MagnoliaDevice.class));
		}
		return out;
	}

	private List<MagnoliaContentCard> parseContentCardDeliveryPayload(String response) throws JsonProcessingException {
		JsonNode arrayNode = magnoliaDeliveryArray(response);
		if (arrayNode == null) {
			return List.of();
		}
		List<MagnoliaContentCard> out = new ArrayList<>();
		for (JsonNode n : arrayNode) {
			if (n == null || n.isNull()) {
				continue;
			}
			out.add(objectMapper.treeToValue(n, MagnoliaContentCard.class));
		}
		return out;
	}

	/**
	 * Magnolia delivery may return a JSON array or an envelope with {@code docs} / {@code results}.
	 */
	private JsonNode magnoliaDeliveryArray(String response) throws JsonProcessingException {
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
			return null;
		}
		return arrayNode;
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
