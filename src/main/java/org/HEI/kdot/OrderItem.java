package org.HEI.kdot;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItem {
    private String orderId;
    private int quantity;
    private Product product;

    public BigDecimal getTotalItemPrice() {
        return product.getCurrentPrice().multiply(new BigDecimal(quantity));
    }
}
