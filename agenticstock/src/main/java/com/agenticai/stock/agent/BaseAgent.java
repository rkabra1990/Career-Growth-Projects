package com.agenticai.stock.agent;

import com.agenticai.stock.model.Action;
import com.agenticai.stock.model.MarketObservation;
import com.agenticai.stock.model.Plan;

public interface BaseAgent {
    MarketObservation observe();
    Plan plan(MarketObservation observation);
    Action act(Plan plan);
    void learn(Action action);
}
