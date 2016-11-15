package org.okarmus.aggregate.processor;

import org.okarmus.transaction.domain.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.summarizingDouble;

/**
 * Created by mateusz on 11.11.16.
 */

@Service
public class TransactionStatisticsCalculator {

    @Value("${ignoredCount}")
    private boolean ignoredProductCount;

    public DoubleSummaryStatistics calculateStatistics(List<Transaction> transactions, int topCount) {
        return transactions.stream().sorted(maxTotalComparator())
                .limit(topCount)
                .flatMap(this::unwindTransactions)
                .collect(summarizingDouble(t -> t));
    }

    private Stream<Double> unwindTransactions(Transaction t) {
        return IntStream.range(0, getProductCount(t))
                .boxed()
                .map(x -> t.getTotalValue().doubleValue());
    }

    private Comparator<Transaction> maxTotalComparator() {
        return comparing((Transaction t) -> t.getTotalValue().doubleValue() * getProductCount(t))
                .reversed();
    }

    private int getProductCount(Transaction transaction) {
        return ignoredProductCount ? 1 : transaction.getQuantity();
    }
}
