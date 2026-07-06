package org.HEI.kdot;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class Order {
    private String id;
    private Instant createdAt;
    private Instant updatedAt;
    private Customer customer;
    private Payement payement;
    private List<OrderItem> orderItems;

    public OrderItem addItem(OrderItem toAdd) {
        this.orderItems.add(toAdd);

        return toAdd;
    }

    public OrderItem removeItem(OrderItem toRemove) {
        this.orderItems.remove(toRemove);

        return toRemove;
    }

    public BigDecimal getTotalCost() {

    }
}
