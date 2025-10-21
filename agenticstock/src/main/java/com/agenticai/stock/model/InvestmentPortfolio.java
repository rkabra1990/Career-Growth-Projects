package com.agenticai.stock.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class InvestmentPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private double buyPrice;
    private LocalDate buyDate;
    private int holdingPeriodDays;
    private String strategy;  // "LongTerm"
    private boolean active = true;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public double getBuyPrice() { return buyPrice; }
    public void setBuyPrice(double buyPrice) { this.buyPrice = buyPrice; }

    public LocalDate getBuyDate() { return buyDate; }
    public void setBuyDate(LocalDate buyDate) { this.buyDate = buyDate; }

    public int getHoldingPeriodDays() { return holdingPeriodDays; }
    public void setHoldingPeriodDays(int holdingPeriodDays) { this.holdingPeriodDays = holdingPeriodDays; }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
