package org.HEI.kdot;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class MobilePayement extends Payement{
    private String transactionRef;
    private String phone;
}
