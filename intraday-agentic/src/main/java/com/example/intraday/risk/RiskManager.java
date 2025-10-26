package com.example.intraday.risk;

import com.example.intraday.broker.BrokerClient;
import com.example.intraday.dto.OrderRequest;
import com.example.intraday.dto.OrderResult;
import com.example.intraday.entities.OrderEntity;
import com.example.intraday.entities.TradeEntity;
import com.example.intraday.repo.OrderRepository;
import com.example.intraday.repo.TradeRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RiskManager {
    private final BrokerClient brokerClient;
    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;
    private final AtomicInteger positions = new AtomicInteger(0);
    private final int maxPosition = 10;


    public RiskManager(BrokerClient brokerClient, OrderRepository orderRepository, TradeRepository tradeRepository) {
        this.brokerClient = brokerClient;
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
    }


    public OrderResult checkAndExecute(OrderRequest req) {
        OrderEntity oe = new OrderEntity();
        oe.setClientOrderId(req.clientOrderId());
        oe.setSymbol(req.symbol());
        oe.setSide(req.side().name());
        oe.setQty(req.qty());
        oe.setPrice(req.price());
        oe.setStatus("NEW");
        oe.setCreatedAt(Instant.now());
        orderRepository.save(oe);


        if (Math.abs(positions.get()) + req.qty() > maxPosition) {
            oe.setStatus("REJECTED");
            orderRepository.save(oe);
            return new OrderResult(null, false, "position limit exceeded");
        }


        OrderResult res = brokerClient.placeOrder(req);
        if (res.success()) {
            oe.setStatus("FILLED");
            orderRepository.save(oe);


            TradeEntity te = new TradeEntity();
            te.setOrderId(oe.getId());
            te.setSymbol(req.symbol());
            te.setSide(req.side().name());
            te.setQty(req.qty());
            te.setPrice(req.price());
            te.setExecutedAt(Instant.now());
            tradeRepository.save(te);


            if (req.side().name().equals("BUY")) positions.addAndGet(req.qty());
            else positions.addAndGet(-req.qty());
        }
        return res;
    }
}
