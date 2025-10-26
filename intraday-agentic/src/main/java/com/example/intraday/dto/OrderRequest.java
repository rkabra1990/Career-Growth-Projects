package com.example.intraday.dto;

public record OrderRequest(String symbol, Side side, int qty, double price, String clientOrderId) {}
