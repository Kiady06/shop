package org.HEI.kdot;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class PriceHistory {
    private String id;
    private BigDecimal price;
    private Instant effectiveFrom;
}
