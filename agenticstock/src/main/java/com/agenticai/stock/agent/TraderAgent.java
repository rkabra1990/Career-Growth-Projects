package com.agenticai.stock.agent;

import com.agenticai.stock.model.*;
import com.agenticai.stock.service.ProfitabilityTracker;

import java.time.LocalDateTime;

public class TraderAgent implements BaseAgent {

    private final ProfitabilityTracker tracker = new ProfitabilityTracker();

    @Override
    public MarketObservation observe() {
        return new MarketObservation("NSE", "HDFCBANK", 1500.00, 1505.25, 1520.00, 1490.50);
    }

    @Override
    public Plan plan(MarketObservation observation) {
        String decision = observation.currentPrice() > observation.openPrice() * 1.01 ? "SELL" : "HOLD";
        return new Plan(decision, "Momentum Strategy");
    }

    @Override
    public Action act(Plan plan) {
        if (plan.decision().equals("SELL")) {
            double buy = 1500.00, sell = 1515.00;
            double profit = sell - buy;
            tracker.recordTrade(new TradeResult("HDFCBANK", buy, sell, profit, profit > 0, LocalDateTime.now()));
        }
        return new Action(plan.decision(), true);
    }

    @Override
    public void learn(Action action) {
        tracker.printSummary();
    }
}
