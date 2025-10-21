package com.agenticai.stock.service;

import com.agenticai.stock.model.TradeOrder;

public interface BrokerService {
    boolean placeOrder(TradeOrder order); // Buy/Sell
    double getBalance();
    TradeOrder fetchOrderStatus(String orderId);
}
