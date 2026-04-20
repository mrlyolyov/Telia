package com.optima.cms.adapter.magnolia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.cms.adapter.magnolia.dto.plan.MagnoliaPlan;
import com.optima.cms.model.plan.FindAllRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.optima.cms.adapter.magnolia.common.MagnoliaConstants.MOCK_PLANS_JSON;

@Component
@Slf4j
public class MagnoliaClient {

    /** Built in {@link com.optima.cms.config.MagnoliaClientConfig#magnoliaWebClient()} (base URL + basic auth). */
    private final WebClient magnoliaWebClient;
    private final ObjectMapper objectMapper;

    public MagnoliaClient(
            @Qualifier("magnoliaWebClient") WebClient magnoliaWebClient,
            ObjectMapper objectMapper) {
        this.magnoliaWebClient = magnoliaWebClient;
        this.objectMapper = objectMapper;
    }

    public List<MagnoliaPlan> getPlans(FindAllRequest request) {
        String response = magnoliaWebClient.get()
                .uri("/.rest/delivery/planSchema/v1")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("Magnolia response: " + response);
        try {
            return objectMapper.readValue(response, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Mock catalog JSON is invalid", e);
        }
    }

    public List<MagnoliaPlan> getPlans2(FindAllRequest request) {
       String response = magnoliaWebClient.get()
                .uri("/.rest/nodes/v1/planSchema/Gold-5G-Unlimited")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("Magnolia response: " + response);
        try {
            return objectMapper.readValue(MOCK_PLANS_JSON, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Mock catalog JSON is invalid", e);
        }
    }
}
