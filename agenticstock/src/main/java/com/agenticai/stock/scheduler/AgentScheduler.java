package com.agenticai.stock.scheduler;

import com.agenticai.stock.agent.TraderAgent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AgentScheduler {

    private final TraderAgent agent;

    public AgentScheduler(TraderAgent agent) {
        this.agent = agent; // Spring injects it automatically
    }

    @Scheduled(fixedRate = 60000) // every minute
    public void runAgentCycle() {
        var obs = agent.observe();
        var plan = agent.plan(obs);
        var act = agent.act(plan);
        agent.learn(act);
    }

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void checkIntradayPositions() {
        // In real scenario: check live price, if target/SL hit → close trade
        // For now, just print placeholder
        System.out.println("🔁 Checking intraday positions...");
    }
}
