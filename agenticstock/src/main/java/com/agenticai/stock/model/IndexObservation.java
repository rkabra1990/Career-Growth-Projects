package com.agenticai.stock.model;

public record IndexObservation(
        String indexName,
        double currentLevel,
        double previousClose,
        double high,
        double low,
        double changePercent
) {}
