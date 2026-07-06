package org.HEI.kdot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    private Product supra;
    private Product skyline;
    private Order order;
    private Customer customer;

    @BeforeEach
    void setUp() {
        supra = Product.builder()
                .id("1")
                .name("Toyota Supra Rz")
                .description("A magnificant tuner car")
                .category(ProductCategory.VEHICLE)
                .priceHistory(new ArrayList<>(List.of(
                        PriceHistory.builder()
                                .id("1")
                                .price(BigDecimal.valueOf(58_000))
                                .effectiveFrom(Instant.parse("2025-01-01T00:00:00Z"))
                                .build(),
                        PriceHistory.builder()
                                .id("2")
                                .price(BigDecimal.valueOf(69_000))
                                .effectiveFrom(Instant.parse("2026-01-01T00:00:00Z"))
                                .build()
                )))
                .build();
        skyline = Product.builder()
                .id("2")
                .name("Nissan Skyline GT-R 34")
                .description("The legendary vehicle from Fast and furious")
                .category(ProductCategory.VEHICLE)
                .priceHistory(new ArrayList<>(List.of(
                        PriceHistory.builder()
                                .id("1")
                                .price(BigDecimal.valueOf(15_000))
                                .effectiveFrom(Instant.parse("2025-01-01T00:00:00Z"))
                                .build(),
                        PriceHistory.builder()
                                .id("2")
                                .price(BigDecimal.valueOf(30_000))
                                .effectiveFrom(Instant.parse("2025-06-01T00:00:00Z"))
                                .build()
                )))
                .build();
        // customer = new Customer(1, "CL001","Dominique", "Torreto","torreto@mail.com","azerty", "+123 456 789");
        customer = Customer.builder()
                .id("1")
                .ref("CL001")
                .firstName("Dominique")
                .lastName("Torreto")
                .email("torreto@mail.com")
                .password("azerty")
                .phone("+123456789")
                .status(UserStatus.ENABLED)
                .deliveryAddress("Ato")
                //.orders(new ArrayList<>())
                .build();
    }
    @Test
    void getTotalCost_withOrderForToday_shouldUseTodaysPrices() {
        order = new Order("1", Instant.now(), Instant.now(), customer,new ArrayList<>(), new ArrayList<>(
                List.of(
                        new OrderItem("1", 2, supra),
                        new OrderItem("2", 1, skyline)
                )
        ));
        assertEquals(BigDecimal.valueOf(168_000),order.getTotalCost());
    }

    @Test
    void getTotalCost_withPastOrder_shouldUsePastPrices() {
        order = new Order("1", Instant.parse("2025-08-01T00:00:00Z"), Instant.parse("2025-08-01T00:00:00Z"), customer, new ArrayList<>(), new ArrayList<>(
                List.of(
                        new OrderItem("1", 3, supra),
                        new OrderItem("2", 10, skyline))));
        assertEquals(BigDecimal.valueOf(474_000), order.getTotalCost());
    }
}