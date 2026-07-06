package org.HEI.kdot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.HEI.kdot.ProductCategory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("1", "Sweater XL", "A thick sweater", Instant.now(), CLOTHING, new ArrayList<PriceHistory>(
                List.of(
                        new PriceHistory("1", BigDecimal.valueOf(30_000), Instant.parse("2026-01-01T08:00:00Z")),
                        new PriceHistory("2", BigDecimal.valueOf(40_000), Instant.parse("2026-04-01T08:00:00Z")),
                        new PriceHistory("3", BigDecimal.valueOf(70_000), Instant.parse("2026-07-06T08:00:00Z")))
        ));
    }

    @Test
    void getCurrentPrice_returns_theLatestPrice() {
        assertEquals(BigDecimal.valueOf(70_000), product.getCurrentPrice());
    }

    @Test
    void getPriceAt_returns_theCorrectPrice() {
        assertEquals(BigDecimal.valueOf(40_000) , product.getPriceAt(Instant.parse("2026-05-26T09:30:00Z")));
        assertEquals(BigDecimal.valueOf(30_000) , product.getPriceAt(Instant.parse("2026-02-26T09:30:00Z")));
    }
}