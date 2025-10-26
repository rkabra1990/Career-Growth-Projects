package com.example.intraday.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AgenticOrchestrator {
    private final WebClient webClient;
    private final String model;
    private final String apiKey;


    public AgenticOrchestrator(WebClient.Builder webClientBuilder,
                               @Value("${agentic.llm.api-url}") String apiUrl,
                               @Value("${agentic.llm.model}") String model,
                               @Value("${agentic.llm.api-key-env}") String apiKeyEnv) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.model = model;
        this.apiKey = System.getenv(apiKeyEnv);
    }


    public Mono<JsonNode> askAgent(String prompt) {
        ObjectNode body = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode();
        body.put("model", model);
        ArrayNode messages = body.putArray("messages");
        messages.addObject().put("role", "system").put("content", "You are an agent that returns a single JSON object describing an action.");
        messages.addObject().put("role", "user").put("content", prompt);


        return webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}
