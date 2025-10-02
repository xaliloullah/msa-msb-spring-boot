package com.khalil.msa.kafka;

import com.khalil.msa.domain.OrderStatus;
import com.khalil.msa.events.StockCheckedEvent;
import com.khalil.msa.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockCheckedConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "stock-checked", groupId = "msa-group")
    public void consume(StockCheckedEvent event) {
        System.out.println("MS-A received StockCheckedEvent: " + event);

        if (event.isAvailable()) {
            orderService.updateStatus(event.getOrderId(), OrderStatus.CONFIRMÉE);
        } else {
            orderService.updateStatus(event.getOrderId(), OrderStatus.REFUSÉE);
        }
    }
}
