package com.agenticai.stock.model;

import java.time.LocalDateTime;

public record IntradayTrade(
        String symbol,
        double entryPrice,
        double exitPrice,
        double stopLoss,
        double targetPrice,
        String direction, // BUY or SELL
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        String status      // OPEN / CLOSED
) {}
