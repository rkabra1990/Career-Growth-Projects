package com.example.intraday.market;

import com.example.intraday.dto.Tick;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class MarketDataSimulator {
    private final Sinks.Many<Tick> sink = Sinks.many().multicast().onBackpressureBuffer();


    @PostConstruct
    public void start() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::emitTick, 0, 500, TimeUnit.MILLISECONDS);
    }


    private void emitTick() {
        String sym = "TST";
        double price = 100 + Math.sin(System.currentTimeMillis() / 10000.0) * 1.5 + Math.random() * 0.5;
        Tick t = new Tick(sym, System.currentTimeMillis(), price, Math.random() * 100);
        sink.tryEmitNext(t);
    }


    public Flux<Tick> ticks() {
        return sink.asFlux();
    }
}
