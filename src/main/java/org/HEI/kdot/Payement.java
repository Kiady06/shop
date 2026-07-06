package org.HEI.kdot;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@SuperBuilder
@Data
public class Payement {
    private String id;
    private BigDecimal amountPaid;
    private Instant createdAt;
    private PayementStatus status;
}
