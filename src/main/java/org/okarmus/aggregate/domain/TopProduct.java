package org.okarmus.aggregate.domain;

/**
 * Created by mateusz on 10.11.16.
 */
public class TopProduct {

    private int matchingId;
    private double totalPrice;
    private double avgPrice;
    private String currency;
    private boolean ignoredProductCount;

    public int getMatchingId() {
        return matchingId;
    }

    public void setMatchingId(int matchingId) {
        this.matchingId = matchingId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isIgnoredProductCount() {
        return ignoredProductCount;
    }

    public void setIgnoredProductCount(boolean ignoredProductCount) {
        this.ignoredProductCount = ignoredProductCount;
    }
}
