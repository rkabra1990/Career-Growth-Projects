package com.agenticai.stock.agent;

import com.agenticai.stock.model.*;
import com.agenticai.stock.service.MarketDataService;
import com.agenticai.stock.service.ProfitabilityTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TraderAgent implements BaseAgent {

    private final ProfitabilityTracker tracker = new ProfitabilityTracker();

    private final MarketDataService marketDataService;

    @Autowired
    public TraderAgent(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @Override
    public MarketObservation observe() {
        // Fetch stock data
        MarketObservation stockObs = marketDataService.getLiveData("HDFCBANK").block();

        // Fetch index data (e.g., NIFTY 50)
        IndexObservation nifty = marketDataService.getIndexData("NIFTY 50");

        // Print index info (optional logging)
        System.out.printf("Index: %s, Change: %.2f%%\n", nifty.indexName(), nifty.changePercent());

        // You can later use nifty.changePercent() in your plan logic
        return stockObs;

    }

    @Override
    public Plan plan(MarketObservation observation) {
        IndexObservation nifty = marketDataService.getIndexData("NIFTY 50");

        String decision;

        if (nifty.changePercent() > 0) {
            // Market bullish → consider buying
            decision = observation.currentPrice() < observation.openPrice() * 1.01 ? "BUY" : "HOLD";
        } else {
            // Market bearish → hold or sell
            decision = observation.currentPrice() > observation.openPrice() * 1.01 ? "SELL" : "HOLD";
        }

        return new Plan(decision, "Index-aware strategy");
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
