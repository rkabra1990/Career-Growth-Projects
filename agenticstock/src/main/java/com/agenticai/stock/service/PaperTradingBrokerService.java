package com.agenticai.stock.service;

import com.agenticai.stock.model.TradeOrder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaperTradingBrokerService implements BrokerService {

    private double balance = 100_000; // ₹1 lakh virtual funds

    @Override
    public boolean placeOrder(TradeOrder order) {
        double cost = order.quantity() * (order.price() == 0 ? getMarketPrice(order.symbol()) : order.price());
        String status = "EXECUTED"; // For paper trading, we execute immediately

        if (order.action().equalsIgnoreCase("BUY")) {
            if (balance >= cost) {
                balance -= cost;
                System.out.printf("📈 Paper BUY executed: %s x%d @ %.2f | New Balance: %.2f%n",
                        order.symbol(), order.quantity(), cost / order.quantity(), balance);
            } else {
                status = "FAILED";
                System.out.println("❌ Insufficient balance for BUY: " + order.symbol());
            }
        } else if (order.action().equalsIgnoreCase("SELL")) {
            balance += cost;
            System.out.printf("📉 Paper SELL executed: %s x%d @ %.2f | New Balance: %.2f%n",
                    order.symbol(), order.quantity(), cost / order.quantity(), balance);
        }

        // Return updated order (with execution status & timestamp)
        TradeOrder executedOrder = new TradeOrder(
                UUID.randomUUID().toString(),
                order.symbol(),
                order.action(),
                order.orderType(),
                order.price(),
                order.quantity(),
                status,
                LocalDateTime.now()
        );

        return true;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public TradeOrder fetchOrderStatus(String orderId) {
        // Mock implementation: In paper trading, all orders executed instantly
        return null;
    }

    private double getMarketPrice(String symbol) {
        // Simulate market price for MARKET orders (you can replace with MarketDataService)
        return 1500 + Math.random() * 20; // Example: 1500-1520 range
    }
}
