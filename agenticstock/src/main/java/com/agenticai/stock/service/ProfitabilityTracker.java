package com.agenticai.stock.service;

import com.agenticai.stock.model.TradeResult;
import java.util.ArrayList;
import java.util.List;

public class ProfitabilityTracker {

    private final List<TradeResult> trades = new ArrayList<>();

    public void recordTrade(TradeResult trade) {
        trades.add(trade);
    }

    public double totalProfit() {
        return trades.stream().mapToDouble(TradeResult::profit).sum();
    }

    public double winRate() {
        if (trades.isEmpty()) return 0.0;
        long wins = trades.stream().filter(TradeResult::successful).count();
        return (wins * 100.0) / trades.size();
    }

    public double averageProfit() {
        return trades.stream().mapToDouble(TradeResult::profit).average().orElse(0.0);
    }

    public void printSummary() {
        System.out.println("------ Profit Summary ------");
        System.out.printf("Total Trades: %d%n", trades.size());
        System.out.printf("Total Profit: ₹%.2f%n", totalProfit());
        System.out.printf("Win Rate: %.2f%%%n", winRate());
        System.out.printf("Average Profit: ₹%.2f%n", averageProfit());
        System.out.println("-----------------------------");
    }
}
