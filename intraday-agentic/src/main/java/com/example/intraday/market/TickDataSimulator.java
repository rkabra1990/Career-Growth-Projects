package com.example.intraday.market;

import com.example.intraday.dto.Tick;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TickDataSimulator {
    private final Sinks.Many<Tick> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final Random rnd = new Random();
    private final Map<String, Double> lastPrice = new HashMap<>();


    private final MarketContextService marketContextService;


    public TickDataSimulator(MarketContextService marketContextService) {
        this.marketContextService = marketContextService;
        marketContextService.getSymbols().forEach(s -> lastPrice.put(s, 100.0 + rnd.nextDouble() * 100));
    }


    @PostConstruct
    public void start() {
// emit a tick every second for demo; in real mode you'd emit 1s ticks aggregated to 1m candles
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::emit, 0, 1, TimeUnit.SECONDS);
    }


    private void emit() {
        List<String> syms = marketContextService.getSymbols();
        for (String sym : syms) {
            double p = lastPrice.get(sym);
// random walk
            double change = (rnd.nextDouble() - 0.5) * 0.5; // +/-0.25
            p = Math.max(1.0, p + change);
            lastPrice.put(sym, p);
            Tick t = new Tick(sym, Instant.now().toEpochMilli(), p, 100 + rnd.nextDouble() * 1000);
            sink.tryEmitNext(t);
        }
    }


    public Flux<Tick> ticks() { return sink.asFlux(); }
}
