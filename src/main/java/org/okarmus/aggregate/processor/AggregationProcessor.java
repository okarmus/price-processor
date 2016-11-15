package org.okarmus.aggregate.processor;

import org.okarmus.aggregate.domain.Matching;
import org.okarmus.aggregate.domain.TopProduct;
import org.okarmus.transaction.domain.Transaction;
import org.okarmus.transaction.repository.TransactionRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * Created by mateusz on 10.11.16.
 */
@Service
public class AggregationProcessor implements ItemProcessor<Matching, TopProduct> {

    @Value("${currency}")
    private String currency;

    @Value("${ignoredCount}")
    private boolean ignoredProductCount;

    private final TransactionRepository transactionRepository;
    private final TransactionStatisticsCalculator statisticsCalculator;

    @Autowired
    public AggregationProcessor(TransactionRepository transactionRepository, TransactionStatisticsCalculator statisticsCalculator) {
        this.transactionRepository = transactionRepository;
        this.statisticsCalculator = statisticsCalculator;
    }

    @Override
    public TopProduct process(Matching matching) throws Exception {
        List<Transaction> transactions = transactionRepository.findByMatchingId(matching.getMatchingId());
        DoubleSummaryStatistics result = statisticsCalculator.calculateStatistics(transactions, matching.getTopPricedCount());
        return buildTotalProduct(matching, result);
    }

    private TopProduct buildTotalProduct(Matching item, DoubleSummaryStatistics result) {
        TopProduct product = new TopProduct();
        product.setMatchingId(item.getMatchingId());
        product.setAvgPrice(round(result.getAverage()));
        product.setTotalPrice(result.getSum());
        product.setCurrency(currency);
        product.setIgnoredProductCount(ignoredProductCount);
        return product;
    }

    private double round(double average) {
        return BigDecimal.valueOf(average)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
