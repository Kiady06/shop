package org.HEI.kdot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

    private Product createProductWithPrice(BigDecimal price) {
        PriceHistory ph = PriceHistory.builder()
                .id("ph-" + price)
                .price(price)
                .effectiveFrom(Instant.now().minus(10, ChronoUnit.DAYS))
                .build();

        return Product.builder()
                .id("prod-" + price)
                .name("Produit test")
                .priceHistory(List.of(ph))
                .build();
    }

    @Nested
    @DisplayName("addItem(OrderItem toAdd)")
    class AddItemTests {

        @Test
        @DisplayName("Ajoute l'item à la liste des orderItems de la commande")
        void shouldAddItemToOrder() {
            // Given : une commande vide
            Order order = Order.builder()
                    .id("o1")
                    .orderItems(new ArrayList<>())
                    .payements(new ArrayList<>())
                    .build();

            OrderItem item = OrderItem.builder()
                    .orderId("oi1")
                    .quantity(2)
                    .product(createProductWithPrice(BigDecimal.TEN))
                    .build();

            // When : on ajoute l'item
            order.addItem(item);

            // Then : l'item se retrouve bien dans la commande
            assertTrue(order.getOrderItems().contains(item));
            assertEquals(1, order.getOrderItems().size());
        }
    }

    @Nested
    @DisplayName("removeItem(OrderItem item)")
    class RemoveItemTests {

        @Test
        @DisplayName("Retire l'item de la liste des orderItems de la commande")
        void shouldRemoveItemFromOrder() {
            // Given : une commande avec un item déjà présent
            OrderItem item = OrderItem.builder()
                    .orderId("oi1")
                    .quantity(1)
                    .product(createProductWithPrice(BigDecimal.TEN))
                    .build();

            List<OrderItem> items = new ArrayList<>();
            items.add(item);

            Order order = Order.builder()
                    .id("o2")
                    .orderItems(items)
                    .payements(new ArrayList<>())
                    .build();

            // When : on retire l'item
            order.removeItem(item);

            // Then : la commande ne contient plus cet item
            assertFalse(order.getOrderItems().contains(item));
            assertEquals(0, order.getOrderItems().size());
        }
    }

    @Nested
    @DisplayName("getTotalCost()")
    class GetTotalCostTests {

        @Test
        @DisplayName("Additionne le prix total de chaque OrderItem")
        void shouldSumAllOrderItemsTotalPrice() {
            // Given : deux OrderItem avec des produits et quantités différentes
            OrderItem item1 = OrderItem.builder()
                    .orderId("oi1")
                    .quantity(2)
                    .product(createProductWithPrice(BigDecimal.valueOf(50))) // 2 * 50 = 100
                    .build();

            OrderItem item2 = OrderItem.builder()
                    .orderId("oi2")
                    .quantity(3)
                    .product(createProductWithPrice(BigDecimal.valueOf(20))) // 3 * 20 = 60
                    .build();

            Order order = Order.builder()
                    .id("o3")
                    .orderItems(new ArrayList<>(List.of(item1, item2)))
                    .payements(new ArrayList<>())
                    .build();

            // When : on calcule le coût total
            BigDecimal total = order.getTotalCost();

            // Then : le total correspond à la somme des deux items (100 + 60 = 160)
            assertEquals(BigDecimal.valueOf(160), total);
        }

        @Test
        @DisplayName("Renvoie zéro si la commande ne contient aucun item")
        void shouldReturnZeroWhenNoItems() {
            // Given : une commande sans aucun item
            Order order = Order.builder()
                    .id("o4")
                    .orderItems(new ArrayList<>())
                    .payements(new ArrayList<>())
                    .build();

            // When
            BigDecimal total = order.getTotalCost();

            // Then
            assertEquals(BigDecimal.ZERO, total);
        }
    }

    @Nested
    @DisplayName("isPaid()")
    class IsPaidTests {

        @Test
        @DisplayName("Renvoie true quand tous les paiements sont DONE et couvrent le coût total")
        void shouldReturnTrueWhenFullyPaidAndAllDone() {
            // Given : une commande avec un item à 100, payée intégralement en un seul paiement DONE
            OrderItem item = OrderItem.builder()
                    .orderId("oi1")
                    .quantity(1)
                    .product(createProductWithPrice(BigDecimal.valueOf(100)))
                    .build();

            Payement payment = Payement.builder()
                    .id("pay1")
                    .amountPaid(BigDecimal.valueOf(100))
                    .createdAt(Instant.now())
                    .status(PayementStatus.DONE)
                    .build();

            Order order = Order.builder()
                    .id("o5")
                    .orderItems(new ArrayList<>(List.of(item)))
                    .payements(new ArrayList<>(List.of(payment)))
                    .build();

            // When / Then
            assertTrue(order.isPaid());
        }

        @Test
        @DisplayName("Renvoie false si un paiement n'est pas DONE, même si le montant total correspond")
        void shouldReturnFalseWhenOnePaymentIsNotDone() {
            // Given : deux paiements, dont un encore en attente
            OrderItem item = OrderItem.builder()
                    .orderId("oi1")
                    .quantity(1)
                    .product(createProductWithPrice(BigDecimal.valueOf(100)))
                    .build();

            Payement donePayment = Payement.builder()
                    .id("pay1")
                    .amountPaid(BigDecimal.valueOf(60))
                    .createdAt(Instant.now())
                    .status(PayementStatus.DONE)
                    .build();

            Payement pendingPayment = Payement.builder()
                    .id("pay2")
                    .amountPaid(BigDecimal.valueOf(40))
                    .createdAt(Instant.now())
                    .status(PayementStatus.PENDING)
                    .build();

            Order order = Order.builder()
                    .id("o6")
                    .orderItems(new ArrayList<>(List.of(item)))
                    .payements(new ArrayList<>(List.of(donePayment, pendingPayment)))
                    .build();

            // When / Then : même si 60 + 40 = 100 (montant correct), un paiement PENDING bloque isPaid()
            assertFalse(order.isPaid());
        }

        @Test
        @DisplayName("Renvoie false si le montant payé est inférieur au coût total")
        void shouldReturnFalseWhenAmountPaidIsLessThanTotalCost() {
            // Given : un item à 100, mais un seul paiement DONE de 50
            OrderItem item = OrderItem.builder()
                    .orderId("oi1")
                    .quantity(1)
                    .product(createProductWithPrice(BigDecimal.valueOf(100)))
                    .build();

            Payement payment = Payement.builder()
                    .id("pay1")
                    .amountPaid(BigDecimal.valueOf(50))
                    .createdAt(Instant.now())
                    .status(PayementStatus.DONE)
                    .build();

            Order order = Order.builder()
                    .id("o7")
                    .orderItems(new ArrayList<>(List.of(item)))
                    .payements(new ArrayList<>(List.of(payment)))
                    .build();

            // When / Then
            assertFalse(order.isPaid());
        }

        @Test
        @DisplayName("Renvoie true pour une commande sans item et sans paiement (cas limite)")
        void shouldReturnTrueWhenNoItemsAndNoPayments() {
            // Given : une commande totalement vide
            Order order = Order.builder()
                    .id("o8")
                    .orderItems(new ArrayList<>())
                    .payements(new ArrayList<>())
                    .build();

            // When / Then : allMatch sur liste vide = true, et 0 == ZERO
            // -> c'est un cas limite à discuter avec le professeur (comportement voulu ?)
            assertTrue(order.isPaid());
        }
    }
}