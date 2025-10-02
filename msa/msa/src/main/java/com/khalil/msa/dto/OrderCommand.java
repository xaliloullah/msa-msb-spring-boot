package com.khalil.msa.dto; 

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCommand {
    private Long productId;
    private Integer quantity;
    private String clientName;
}
