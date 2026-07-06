package org.HEI.kdot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    private final Instant sixMonthsAgo = Instant.now().minus(180, ChronoUnit.DAYS);
    private final Instant threeMonthsAgo = Instant.now().minus(90, ChronoUnit.DAYS);
    private final Instant oneMonthAgo = Instant.now().minus(30, ChronoUnit.DAYS);

    @Nested
    @DisplayName("getPriceAt(Instant t)")
    class GetPriceAtTests {

        @Test
        @DisplayName("Renvoie le prix correspondant à la période où t se situe")
        void shouldReturnPriceEffectiveAtGivenInstant() {
            // Given : un produit avec un historique de 3 changements de prix
            PriceHistory oldPrice = PriceHistory.builder()
                    .id("ph1")
                    .price(BigDecimal.valueOf(100))
                    .effectiveFrom(sixMonthsAgo)
                    .build();

            PriceHistory midPrice = PriceHistory.builder()
                    .id("ph2")
                    .price(BigDecimal.valueOf(120))
                    .effectiveFrom(threeMonthsAgo)
                    .build();

            PriceHistory recentPrice = PriceHistory.builder()
                    .id("ph3")
                    .price(BigDecimal.valueOf(150))
                    .effectiveFrom(oneMonthAgo)
                    .build();

            Product product = Product.builder()
                    .id("p1")
                    .name("Casque audio")
                    .priceHistory(List.of(oldPrice, midPrice, recentPrice))
                    .build();

            // When : on demande le prix à un instant situé entre midPrice et recentPrice
            Instant t = threeMonthsAgo.plus(10, ChronoUnit.DAYS);
            BigDecimal result = product.getPriceAt(t);

            // Then : c'est le prix "midPrice" qui s'applique (le dernier effectiveFrom <= t)
            assertEquals(BigDecimal.valueOf(120), result);
        }

        @Test
        @DisplayName("Renvoie exactement le prix dont effectiveFrom est égal à t")
        void shouldReturnPriceWhenInstantMatchesEffectiveFromExactly() {
            // Given : un produit avec un seul changement de prix
            PriceHistory price = PriceHistory.builder()
                    .id("ph1")
                    .price(BigDecimal.valueOf(200))
                    .effectiveFrom(oneMonthAgo)
                    .build();

            Product product = Product.builder()
                    .id("p2")
                    .name("Clavier mécanique")
                    .priceHistory(List.of(price))
                    .build();

            // When : on interroge exactement à l'instant effectiveFrom
            BigDecimal result = product.getPriceAt(oneMonthAgo);

            // Then : ce prix est bien renvoyé (borne incluse)
            assertEquals(BigDecimal.valueOf(200), result);
        }

        @Test
        @DisplayName("Lève une exception si aucun prix n'existe encore à cet instant")
        void shouldThrowWhenNoPriceExistsBeforeGivenInstant() {
            // Given : un produit dont le premier prix ne commence que dans 1 mois
            PriceHistory futurePrice = PriceHistory.builder()
                    .id("ph1")
                    .price(BigDecimal.valueOf(300))
                    .effectiveFrom(Instant.now().plus(30, ChronoUnit.DAYS))
                    .build();

            Product product = Product.builder()
                    .id("p3")
                    .name("Souris gamer")
                    .priceHistory(List.of(futurePrice))
                    .build();

            // When / Then : demander le prix à "maintenant" doit lever une exception
            assertThrows(IllegalStateException.class, () -> product.getPriceAt(Instant.now()));
        }
    }

    @Nested
    @DisplayName("getCurrentPrice()")
    class GetCurrentPriceTests {

        @Test
        @DisplayName("Renvoie le prix le plus récent par rapport à maintenant")
        void shouldReturnMostRecentPrice() {
            // Given : un produit avec un ancien prix et un prix récent
            PriceHistory oldPrice = PriceHistory.builder()
                    .id("ph1")
                    .price(BigDecimal.valueOf(100))
                    .effectiveFrom(sixMonthsAgo)
                    .build();

            PriceHistory currentPrice = PriceHistory.builder()
                    .id("ph2")
                    .price(BigDecimal.valueOf(180))
                    .effectiveFrom(oneMonthAgo)
                    .build();

            Product product = Product.builder()
                    .id("p4")
                    .name("Écran 27 pouces")
                    .priceHistory(List.of(oldPrice, currentPrice))
                    .build();

            // When : on demande le prix actuel
            BigDecimal result = product.getCurrentPrice();

            // Then : c'est bien le prix le plus récent qui est renvoyé
            assertEquals(BigDecimal.valueOf(180), result);
        }

        @Test
        @DisplayName("Lève une exception si le produit n'a encore aucun prix effectif")
        void shouldThrowWhenNoPriceIsEffectiveYet() {
            // Given : un produit dont le seul prix ne sera effectif que dans le futur
            PriceHistory futurePrice = PriceHistory.builder()
                    .id("ph1")
                    .price(BigDecimal.valueOf(90))
                    .effectiveFrom(Instant.now().plus(10, ChronoUnit.DAYS))
                    .build();

            Product product = Product.builder()
                    .id("p5")
                    .name("Webcam HD")
                    .priceHistory(List.of(futurePrice))
                    .build();

            // When / Then : getCurrentPrice() doit lever une exception
            assertThrows(IllegalStateException.class, product::getCurrentPrice);
        }
    }
}