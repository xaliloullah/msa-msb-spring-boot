package com.khalil.msb.kafka;

import com.khalil.msb.events.OrderCreatedEvent;
import com.khalil.msb.events.StockCheckedEvent;
import com.khalil.msb.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final StockService stockService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "order-created", groupId = "msb-group")
    public void consume(OrderCreatedEvent event) {
        System.out.println("MS-B received OrderCreatedEvent: " + event);

        // Vérifier le stock et générer l'événement
        StockCheckedEvent result = stockService.checkStock(
                event.getOrderId(),
                event.getProductId(),
                event.getQuantity()
        );

        // Publier le résultat vers Kafka
        kafkaTemplate.send("stock-checked", result);
    }
}
