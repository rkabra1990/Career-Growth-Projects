package com.agenticai.stock.model;

public record MarketObservation(
        String exchange,
        String symbol,
        double openPrice,
        double currentPrice,
        double high,
        double low
) {}