package com.khalil.msb.controller;

import com.khalil.msb.domain.Stock;
import com.khalil.msb.events.StockCheckedEvent;
import com.khalil.msb.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/{productId}")
    public Stock getStock(@PathVariable Long productId) {
        return stockService.getStock(productId);
    }

    /**
     * Vérification manuelle du stock via REST (utile pour tester sans Kafka).
     * Exemple :
     *   POST /stocks/1/check?orderId=5&quantity=3
     */
    @PostMapping("/{productId}/check")
    public StockCheckedEvent checkStock(
            @PathVariable Long productId,
            @RequestParam Long orderId,
            @RequestParam int quantity) {

        return stockService.checkStock(orderId, productId, quantity);
    }
}
