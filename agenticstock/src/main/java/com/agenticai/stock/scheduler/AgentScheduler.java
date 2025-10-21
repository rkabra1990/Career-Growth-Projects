package com.agenticai.stock.scheduler;

import com.agenticai.stock.agent.TraderAgent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AgentScheduler {

    private final TraderAgent agent = new TraderAgent();

    @Scheduled(fixedRate = 60000) // every minute
    public void runAgentCycle() {
        var obs = agent.observe();
        var plan = agent.plan(obs);
        var act = agent.act(plan);
        agent.learn(act);
    }
}
