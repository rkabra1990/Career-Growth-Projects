package com.agenticai.stock.service;

import com.agenticai.stock.model.TradeOrder;
import org.springframework.stereotype.Service;

@Service
public class PaperTradingBrokerService implements BrokerService {

    private double balance = 100000; // ₹1 lakh virtual funds

    @Override
    public boolean placeOrder(TradeOrder order) {
        double cost = order.quantity() * order.price();
        if (order.action().equalsIgnoreCase("BUY")) {
            balance -= cost;
            System.out.println("Paper BUY executed for " + order.symbol() + " @ " + order.price());
        } else if (order.action().equalsIgnoreCase("SELL")) {
            balance += cost;
            System.out.println("Paper SELL executed for " + order.symbol() + " @ " + order.price());
        }
        return true;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public TradeOrder fetchOrderStatus(String orderId) {
        return null; // Not applicable in mock
    }
}
