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
        FundamentalData fa = marketDataService.getFundamentalData("HDFCBANK");

        String decision;
        String strategy;

        // 🧮 Long-Term Investing Logic (Fundamental Analysis)
        if (fa.peRatio() < 25 && fa.roe() > 15 && fa.debtToEquity() < 1 && fa.dividendYield() > 2) {
            decision = "BUY_FOR_LONG_TERM";
            strategy = "Fundamental";
        }
        // === Intraday Logic (Topic 7) ===
        else if (isIntradayWindow() && Math.abs(observation.changePercent()) > 0.5) {
            decision = observation.changePercent() > 0 ? "INTRADAY_BUY" : "INTRADAY_SELL";
            strategy = "Intraday";
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
            case "INTRADAY_BUY", "INTRADAY_SELL" -> executeIntradayTrade(plan.decision());
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

    private boolean isIntradayWindow() {
        var now = java.time.LocalTime.now();
        return now.isAfter(java.time.LocalTime.of(9, 15)) &&
                now.isBefore(java.time.LocalTime.of(15, 15));
    }
    private void executeIntradayTrade(String action) {
        String symbol = "HDFCBANK";
        MarketObservation obs = marketDataService.getLiveData(symbol).block();

        double entryPrice = obs.currentPrice();
        double stopLoss = entryPrice * (action.equals("INTRADAY_BUY") ? 0.995 : 1.005);
        double target = entryPrice * (action.equals("INTRADAY_BUY") ? 1.01 : 0.99);

        // Place entry order
        TradeOrder entryOrder = new TradeOrder(
                null,
                symbol,
                action.equals("INTRADAY_BUY") ? "BUY" : "SELL",
                "MARKET",
                entryPrice,
                10,
                "EXECUTED",
                LocalDateTime.now()
        );
        brokerService.placeOrder(entryOrder);

        System.out.printf("📊 %s Entry @ %.2f | Target: %.2f | SL: %.2f%n",
                symbol, entryPrice, target, stopLoss);

        // Simulate price move (for paper trading)
        boolean hitTarget = Math.random() > 0.4;
        double exitPrice = hitTarget ? target : stopLoss;
        String exitAction = action.equals("INTRADAY_BUY") ? "SELL" : "BUY";

        // Place exit order
        TradeOrder exitOrder = new TradeOrder(
                null,
                symbol,
                exitAction,
                "MARKET",
                exitPrice,
                10,
                "EXECUTED",
                LocalDateTime.now()
        );
        brokerService.placeOrder(exitOrder);

        double profit = action.equals("INTRADAY_BUY")
                ? (exitPrice - entryPrice) * 10
                : (entryPrice - exitPrice) * 10;

        tracker.recordTrade(new TradeResult(symbol, entryPrice, exitPrice, profit, profit > 0, LocalDateTime.now()));

        System.out.printf("💰 Intraday %s Trade Complete | Entry: %.2f Exit: %.2f | Profit: %.2f%n",
                action, entryPrice, exitPrice, profit);
    }

}
