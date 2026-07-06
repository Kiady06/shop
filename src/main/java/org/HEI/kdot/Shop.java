package org.HEI.kdot;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@Data
@ToString(callSuper = true)
public class Shop {
    private String shopName;
    private String nif;
    private String stat;
    private List<Product> products;

    public List<Product> filterProductsBy(ProductCategory category, BigDecimal minPrice, BigDecimal maxPrice) {
        return products.stream()
                .filter(product -> product.getCategory().equals(category))
                .filter(product -> {
                    BigDecimal price = product.getCurrentPrice();
                    return price.compareTo(minPrice) >= 0 && price.compareTo(maxPrice) <= 0;
                })
                .toList();
    }

}
