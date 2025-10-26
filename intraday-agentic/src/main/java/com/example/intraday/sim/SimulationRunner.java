package com.example.intraday.sim;

import com.example.intraday.market.TickDataSimulator;
import com.example.intraday.strategy.StrategyService;
import org.springframework.stereotype.Component;

@Component
public class SimulationRunner {
    public SimulationRunner(TickDataSimulator simulator, StrategyService strategy) {
        strategy.start(simulator.ticks());
    }
}
