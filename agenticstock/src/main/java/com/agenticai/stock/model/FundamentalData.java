package com.agenticai.stock.model;

public record FundamentalData(
        String symbol,
        double peRatio,
        double pbRatio,
        double roe,
        double eps,
        double debtToEquity,
        double dividendYield
) {}
