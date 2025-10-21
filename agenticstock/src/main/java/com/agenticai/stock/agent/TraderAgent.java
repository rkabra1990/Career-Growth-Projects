package com.agenticai.stock.agent;

import com.agenticai.stock.model.*;
import com.agenticai.stock.repository.PortfolioRepository;
import com.agenticai.stock.service.BrokerService;
import com.agenticai.stock.service.MarketDataService;
import com.agenticai.stock.service.ProfitabilityTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class TraderAgent implements BaseAgent {

    private final ProfitabilityTracker tracker = new ProfitabilityTracker();

    private final MarketDataService marketDataService;
    private final BrokerService brokerService;
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public TraderAgent(MarketDataService marketDataService,BrokerService brokerService,
                       PortfolioRepository portfolioRepository) {
        this.marketDataService = marketDataService;
        this.brokerService = brokerService;
        this.portfolioRepository = portfolioRepository;
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
        String strategy;

        // === Long-Term Investing Logic ===
        if (observation.peRatio() < 25 && observation.dividendYield() > 2.0) {
            decision = "BUY_FOR_LONG_TERM";
            strategy = "LongTerm";
        }
        // === Short-term Trading Logic ===
        else if (nifty.changePercent() > 0 && observation.currentPrice() < observation.openPrice() * 1.01) {
            decision = "BUY";
            strategy = "Trading";
        } else if (nifty.changePercent() < 0 && observation.currentPrice() > observation.openPrice() * 1.01) {
            decision = "SELL";
            strategy = "Trading";
        } else {
            decision = "HOLD";
            strategy = "Neutral";
        }

        return new Plan(decision, strategy);
    }

    @Override
    public Action act(Plan plan) {
        switch (plan.decision()) {
            case "BUY_FOR_LONG_TERM" -> {
                InvestmentPortfolio p = new InvestmentPortfolio();
                p.setSymbol("HDFCBANK");
                p.setBuyPrice(1500.00);
                p.setBuyDate(LocalDate.now());
                p.setHoldingPeriodDays(365);
                p.setStrategy("LongTerm");
                portfolioRepository.save(p);
                System.out.println("✅ Long-term investment added to portfolio: HDFCBANK");
            }
            case "SELL" -> {
                double buy = 1500.00, sell = 1515.00;
                double profit = sell - buy;
                tracker.recordTrade(new TradeResult("HDFCBANK", buy, sell, profit, profit > 0, LocalDateTime.now()));
            }
            default -> System.out.println("⚙️ No trading action performed.");
        }

        return new Action(plan.decision(), true);
    }

    @Override
    public void learn(Action action) {
        tracker.printSummary();
    }
}
