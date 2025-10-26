package com.example.intraday.strategy;

import com.example.intraday.dto.OrderRequest;
import com.example.intraday.dto.Side;
import com.example.intraday.dto.Tick;
import com.example.intraday.risk.RiskManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class StrategyService {
    private final RiskManager riskManager;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Map<String, Deque<Double>> priceWindow = new HashMap<>();


    public StrategyService(RiskManager riskManager) {
        this.riskManager = riskManager;
    }


    public void start(Flux<Tick> ticks) {
        ticks.subscribeOn(Schedulers.boundedElastic())
                .subscribe(this::onTick, err -> {/* log or handle */});
    }


    private void onTick(Tick tick) {
        priceWindow.computeIfAbsent(tick.symbol(), s -> new ArrayDeque<>());
        Deque<Double> dq = priceWindow.get(tick.symbol());
        dq.addLast(tick.price());
        if (dq.size() > 20) dq.removeFirst();


        if (dq.size() >= 20) {
            double first = dq.peekFirst();
            double last = dq.peekLast();
            double change = (last - first) / first;
            if (change > 0.002) {
                submitOrder(tick, Side.BUY, 1);
            } else if (change < -0.002) {
                submitOrder(tick, Side.SELL, 1);
            }
        }
    }


    private void submitOrder(Tick tick, Side side, int qty) {
        OrderRequest req = new OrderRequest(tick.symbol(), side, qty, tick.price(), "cli-" + UUID.randomUUID());
        CompletableFuture.supplyAsync(() -> riskManager.checkAndExecute(req), executor)
                .thenAccept(result -> {
// TODO: persist result or emit event
                });
    }
}
