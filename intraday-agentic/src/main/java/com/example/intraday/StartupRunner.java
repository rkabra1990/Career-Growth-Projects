package com.example.intraday;

import com.example.intraday.market.MarketDataSimulator;
import com.example.intraday.strategy.StrategyService;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner {
    public StartupRunner(MarketDataSimulator simulator, StrategyService strategy) {
        strategy.start(simulator.ticks());
    }
}
