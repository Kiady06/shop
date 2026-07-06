package org.HEI.kdot;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
public class Product {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private ProductCategory category;
    private List<PriceHistory> priceHistory;

    public BigDecimal getPriceAt(Instant t) {
        return priceHistory.stream()
                .filter(ph -> !ph.getEffectiveFrom().isAfter(t))
                .max(Comparator.comparing(PriceHistory::getEffectiveFrom))
                .map(PriceHistory::getPrice)
                .orElseThrow(() -> new IllegalStateException("Aucun prix disponible à l'instant " + t));
    }

    public BigDecimal getCurrentPrice() {
        return getPriceAt(Instant.now());
    }
}
