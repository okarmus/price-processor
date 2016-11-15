package org.okarmus.aggregate.domain;

/**
 * Created by mateusz on 10.11.16.
 */
public class Matching {

    private int matchingId;
    private int topPricedCount;

    public int getMatchingId() {
        return matchingId;
    }

    public void setMatchingId(int matchingId) {
        this.matchingId = matchingId;
    }

    public int getTopPricedCount() {
        return topPricedCount;
    }

    public void setTopPricedCount(int topPricedCount) {
        this.topPricedCount = topPricedCount;
    }
}
