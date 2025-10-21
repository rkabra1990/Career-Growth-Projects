package com.agenticai.stock.service;

import com.agenticai.stock.model.IndexObservation;
import com.agenticai.stock.model.MarketObservation;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MarketDataService {

    private final WebClient webClient = WebClient.create("https://www.nseindia.com/api/");

    public Mono<MarketObservation> getLiveData(String symbol) {
        // Example URL: fetch OHLC for a symbol (mock structure)
        return webClient.get()
                .uri("/quote-equity?symbol={symbol}", symbol)
                .retrieve()
                .bodyToMono(MarketObservation.class);
    }

    public IndexObservation getIndexData(String indexName) {
        // Mock data for now
        double prevClose = 18000;
        double current = 18200;
        double high = 18300;
        double low = 17950;
        double changePercent = ((current - prevClose) / prevClose) * 100;

        return new IndexObservation(indexName, current, prevClose, high, low, changePercent);
    }

}
