package com.example.intraday.broker;

import com.example.intraday.dto.OrderRequest;
import com.example.intraday.dto.OrderResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BrokerClient {
    public OrderResult placeOrder(OrderRequest req) {
        try { Thread.sleep(40); } catch (InterruptedException ignored) {}
        String orderId = "SIM-" + UUID.randomUUID();
        return new OrderResult(orderId, true, "Filled at " + req.price());
    }
}
