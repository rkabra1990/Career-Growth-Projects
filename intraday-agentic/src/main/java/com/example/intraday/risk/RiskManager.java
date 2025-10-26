package com.example.intraday.risk;

import com.example.intraday.broker.BrokerClient;
import com.example.intraday.dto.OrderRequest;
import com.example.intraday.dto.OrderResult;
import com.example.intraday.dto.Side;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RiskManager {
    private final BrokerClient brokerClient;
    private final AtomicInteger positions = new AtomicInteger(0);
    private final int maxPosition = 10;


    public RiskManager(BrokerClient brokerClient) {
        this.brokerClient = brokerClient;
    }


    public OrderResult checkAndExecute(OrderRequest req) {
        if (Math.abs(positions.get()) + req.qty() > maxPosition) {
            return new OrderResult(null, false, "position limit exceeded");
        }
        OrderResult res = brokerClient.placeOrder(req);
        if (res.success()) {
            if (req.side() == Side.BUY) positions.addAndGet(req.qty());
            else positions.addAndGet(-req.qty());
        }
        return res;
    }
}
