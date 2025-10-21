package com.agenticai.stock.model;

public record IndexObservation(
        String indexName,
        double changePercent
) {}
