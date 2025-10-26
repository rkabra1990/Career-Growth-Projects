package com.example.intraday.dto;

public record OrderResult(String orderId, boolean success, String message) {}
