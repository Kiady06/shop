package org.HEI.kdot;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@Data
@ToString(callSuper = true)
public class Shop {
    public String shopName;
    public String nif;
    public String stat;
}
