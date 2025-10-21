package com.agenticai.stock.model;

import java.time.LocalDateTime;

public record TradeResult(
        String symbol,
        double buyPrice,
        double sellPrice,
        double profit,
        boolean successful,
        LocalDateTime timestamp
) {}
