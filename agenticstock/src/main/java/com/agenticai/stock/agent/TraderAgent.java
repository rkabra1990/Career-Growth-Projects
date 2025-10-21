package com.agenticai.stock.agent;

import com.agenticai.stock.model.*;

public class TraderAgent implements BaseAgent {

    @Override
    public MarketObservation observe() {
        // Later: connect to NSE live API
        return new MarketObservation(
                "NSE",           // exchange
                "HDFCBANK",      // symbol
                1500.00,         // openPrice
                1500.25,         // currentPrice
                1520.00,         // high
                1490.50          // low
        );

    }

    @Override
    public Plan plan(MarketObservation observation) {
        // Placeholder logic: if price rises >1%, plan to sell
        String decision = observation.currentPrice() > observation.openPrice() * 1.01 ? "SELL" : "HOLD";
        return new Plan(decision, "Momentum strategy");
    }

    @Override
    public Action act(Plan plan) {
        System.out.println("Executing action: " + plan.decision());
        return new Action(plan.decision(), true);
    }

    @Override
    public void learn(Action action) {
        System.out.println("Learning from outcome of: " + action.decision());
    }
}
