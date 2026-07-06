package org.HEI.kdot;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Customer extends User {
    private String deliveryAddress;
    private List<Order> orders;

    public List<Order> getRecentOrders() {
        return orders.stream()
                .filter(
                        o -> o.getCreatedAt().isAfter(
                                Instant.now().atZone(ZoneOffset.UTC)
                                        .minusMonths(6)
                                        .toInstant()
                        )
                )
                .toList();
    }
}
