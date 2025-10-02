package com.khalil.msa.service; 

import com.khalil.msa.domain.Order;
import com.khalil.msa.domain.OrderStatus;
import com.khalil.msa.dto.OrderCommand;
import com.khalil.msa.events.OrderCreatedEvent; 
import com.khalil.msa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service; 


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;   
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public Order createOrder(OrderCommand command) {
        Order order = Order.builder()
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .clientName(command.getClientName())
                .status(OrderStatus.EN_ATTENTE)
                .build();

        orderRepository.save(order);   

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .clientName(order.getClientName())
                .build();

        kafkaTemplate.send("order-created", event);
        
        return order;
    }

    public Order updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
