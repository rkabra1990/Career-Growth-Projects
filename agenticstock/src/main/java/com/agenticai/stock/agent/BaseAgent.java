package com.agenticai.stock.agent;

import com.agenticai.stock.model.*;

public interface BaseAgent {
    MarketObservation observe();
    Plan plan(MarketObservation observation);
    Action act(Plan plan);
    void learn(Action action);
}
