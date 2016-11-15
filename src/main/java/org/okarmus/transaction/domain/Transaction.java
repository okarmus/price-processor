package org.okarmus.transaction.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by mateusz on 09.11.16.
 */

@Entity
public class Transaction {

    @Id
    private int id;
    private BigDecimal totalValue;
    private int quantity;
    private int matchingId;

    public Transaction() {}

    public Transaction(int id, BigDecimal totalValue, int quantity, int matchingId) {
        this.id = id;
        this.totalValue = totalValue;
        this.quantity = quantity;
        this.matchingId = matchingId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMatchingId() {
        return matchingId;
    }

    public void setMatchingId(int matchingId) {
        this.matchingId = matchingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (id != that.id) return false;
        if (quantity != that.quantity) return false;
        if (matchingId != that.matchingId) return false;
        return totalValue != null ? totalValue.equals(that.totalValue) : that.totalValue == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (totalValue != null ? totalValue.hashCode() : 0);
        result = 31 * result + quantity;
        result = 31 * result + matchingId;
        return result;
    }
}
