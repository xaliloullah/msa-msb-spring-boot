package com.khalil.msb.service;

import com.khalil.msb.domain.Stock;
import com.khalil.msb.events.StockCheckedEvent;
import com.khalil.msb.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    /**
     * Vérifie la disponibilité du stock pour un produit
     * @param orderId identifiant de la commande (reçu depuis MS-A via OrderCreatedEvent)
     * @param productId identifiant du produit
     * @param quantity quantité demandée
     * @return StockCheckedEvent indiquant si le stock est suffisant
     */
    public StockCheckedEvent checkStock(Long orderId, Long productId, int quantity) {
        Stock stock = stockRepository.findById(productId).orElse(null);

        boolean available = stock != null && stock.getQuantity() >= quantity;

        if (available) {
            stock.setQuantity(stock.getQuantity() - quantity);
            stockRepository.save(stock);
        }

        return StockCheckedEvent.builder()
                .orderId(orderId)
                .productId(productId)
                .available(available)
                .build();
    }

    public Stock getStock(Long productId) {
        return stockRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit inexistant"));
    }
}
