package com.example.intraday.market;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class MarketContextService {
    private final String marketName;
    private final ZoneId timezone;
    private final LocalTime open;
    private final LocalTime close;
    private final LocalTime squareoff;
    private final List<String> symbols;


    public MarketContextService(
            @Value("${market.name:NSE}") String marketName,
            @Value("${market.timezone:Asia/Kolkata}") String tz,
            @Value("${market.trading-hours.open:09:15}") String open,
            @Value("${market.trading-hours.close:15:30}") String close,
            @Value("${market.squareoff-time:15:25}") String squareoff,
            @Value("${market.symbols:}") List<String> symbols
    ) {
        this.marketName = marketName;
        this.timezone = ZoneId.of(tz);
        this.open = LocalTime.parse(open);
        this.close = LocalTime.parse(close);
        this.squareoff = LocalTime.parse(squareoff);
        this.symbols = symbols;
    }


    public String getMarketName() { return marketName; }
    public ZoneId getTimezone() { return timezone; }
    public LocalTime getOpen() { return open; }
    public LocalTime getClose() { return close; }
    public LocalTime getSquareoff() { return squareoff; }
    public List<String> getSymbols() { return symbols; }


    public boolean isTradingTime(java.time.ZonedDateTime now) {
        java.time.ZonedDateTime z = now.withZoneSameInstant(timezone);
        java.time.LocalTime t = z.toLocalTime();
        return !t.isBefore(open) && t.isBefore(close);
    }
}
