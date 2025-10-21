package com.agenticai.stock.model;

public record MarketObservation(
        String symbol,
        double openPrice,
        double currentPrice,
        double high,
        double low,
        double peRatio,
        double dividendYield
) {}