package com.khalil.msb.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCheckedEvent {
    private Long orderId;
    private Long productId;
    private boolean available;
}
