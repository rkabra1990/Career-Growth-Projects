package com.example.intraday.dto;

public record Tick(String symbol, long timestamp, double price, double volume) {}
