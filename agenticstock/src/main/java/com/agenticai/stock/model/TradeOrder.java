package com.agenticai.stock.model;

import java.time.LocalDateTime;

public record TradeOrder(
        String symbol,           // Stock symbol, e.g., "TCS", "RELIANCE"
        String action,           // "BUY" or "SELL"
        int quantity,            // Number of shares
        double price,            // Price per share
        LocalDateTime timestamp, // When trade was made
        String orderId           // Optional: generated order ID
) {}
