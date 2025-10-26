package com.agenticai.stock.model;

import java.time.LocalDateTime;

public record TradeOrder(
        String orderId,     // unique ID
        String symbol,      // stock symbol
        String action,      // BUY / SELL
        String orderType,   // MARKET / LIMIT / STOP_LOSS
        double price,       // price for LIMIT / STOP_LOSS, 0 for MARKET
        int quantity,
        String status,      // PENDING / EXECUTED
        LocalDateTime createdAt
) {}
