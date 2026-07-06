package org.HEI.kdot;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CardPayement extends Payement{
    private String cardNumber;
    private Date expirationDate;
    private String cvc;
    private String bankCompany;
}
