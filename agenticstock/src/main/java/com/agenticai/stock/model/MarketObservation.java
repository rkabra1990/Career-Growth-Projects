package com.agenticai.stock.model;

public record MarketObservation(
        String symbol,
        double openPrice,
        double currentPrice,
        double highPrice,
        double lowPrice,
        double volume,
        double peRatio,
        double dividendYield,
        double changePercent // <-- added field
) {}