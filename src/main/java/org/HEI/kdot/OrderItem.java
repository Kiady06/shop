package org.HEI.kdot;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class OrderItem {
    private String orderId;
    private int quantity;
    private Product product;

    public BigDecimal getTotalItemPrice() {
        return product.getCurrentPrice().multiply(new BigDecimal(quantity));
    }

    public BigDecimal getTotalItemPrice(Instant t) {
        return product.getPriceAt(t).multiply(new BigDecimal(quantity));
    }
}
