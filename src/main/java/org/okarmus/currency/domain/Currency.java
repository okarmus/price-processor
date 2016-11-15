package org.okarmus.currency.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by mateusz on 09.11.16.
 */

@Entity
public class Currency {

    @Id
    private String currency;
    private BigDecimal ratio;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "currency: " + currency + ", ratio: " + ratio;
    }
}
