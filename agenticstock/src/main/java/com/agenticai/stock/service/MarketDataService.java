package com.agenticai.stock.service;

import com.agenticai.stock.model.IndexObservation;
import com.agenticai.stock.model.MarketObservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MarketDataService {

    private final WebClient webClient;

    @Value("${stock.api.base-url:https://api.example.com}") // replace with real API base URL or NSE endpoint
    private String baseUrl;

    public MarketDataService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * Fetches live stock data and maps it into MarketObservation
     */
    public Mono<MarketObservation> getLiveData(String symbol) {
        return webClient.get()
                .uri("/quote-equity?symbol={symbol}", symbol)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    // ===== Extract fields safely =====
                    Map<String, Object> data = (Map<String, Object>) response.get("data");

                    double openPrice = getDouble(data, "open");
                    double currentPrice = getDouble(data, "lastPrice");
                    double highPrice = getDouble(data, "dayHigh");
                    double lowPrice = getDouble(data, "dayLow");
                    double volume = getDouble(data, "quantityTraded");
                    double peRatio = getDouble(data, "pE");
                    double dividendYield = getDouble(data, "dividendYield");

                    double changePercent = ((currentPrice - openPrice) / openPrice) * 100;

                    return new MarketObservation(
                            symbol,
                            openPrice,
                            currentPrice,
                            highPrice,
                            lowPrice,
                            volume,
                            peRatio,
                            dividendYield,
                            changePercent
                    );
                })
                .onErrorResume(e -> {
                    System.err.println("❌ Error fetching data for " + symbol + ": " + e.getMessage());
                    return Mono.just(new MarketObservation(symbol, 0, 0, 0, 0, 0, 0, 0, 0));
                });
    }

    /**
     * Fetches basic index data (like NIFTY, SENSEX)
     */
    public IndexObservation getIndexData(String indexName) {
        // This can later be an API call; currently mocked for demonstration
        double changePercent = Math.random() * 2 - 1; // simulate -1% to +1% movement
        return new IndexObservation(indexName, changePercent);
    }

    // ===== Helper to parse numbers safely =====
    private double getDouble(Map<String, Object> map, String key) {
        try {
            Object val = map.get(key);
            if (val instanceof Number n) return n.doubleValue();
            if (val instanceof String s) return Double.parseDouble(s);
        } catch (Exception ignored) {}
        return 0.0;
    }
}
