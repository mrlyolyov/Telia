package com.optima.cms.adapter.magnolia;

import com.optima.cms.adapter.magnolia.model.MagnoliaPlanResponse;
import com.optima.cms.model.PlanFindAllRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class MagnoliaApiClient {

    private final WebClient webClient;

    public MagnoliaApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public MagnoliaPlanResponse getPlans(PlanFindAllRequest request) {
        return webClient.post()
                .uri("/plans/findall")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(MagnoliaPlanResponse.class)
                .block(); // for now OK
    }

    public String testCall() {
        return webClient.get()
                .uri("https://jsonplaceholder.typicode.com/posts/1")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
