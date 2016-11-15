package org.okarmus.aggregate.processor

import org.okarmus.aggregate.domain.Matching
import org.okarmus.aggregate.domain.TopProduct
import org.okarmus.currency.domain.Currency
import org.okarmus.transaction.domain.Transaction
import org.okarmus.transaction.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

/**
 * Created by mateusz on 11.11.16.
 */
class AggregationProcessorTest extends Specification {

    AggregationProcessor underTest

    TransactionRepository transactionRepository
    TransactionStatisticsCalculator statisticsCalculator

    List<Transaction> transactions1 = [new Transaction(1, 1000, 2, 1), new Transaction(3, 150, 10, 1), new Transaction(5, 1500, 1, 1)]
    List<Transaction> transactions2 = [new Transaction(2, 300, 2, 2), new Transaction(4, 600, 5, 2)]

    def currency = "EUR"
    def ignoreProductCount = false

    def setup() {
        transactionRepository = createTransactionRepository()
        statisticsCalculator = createStatisticsCalculator()

        underTest = new AggregationProcessor(transactionRepository, statisticsCalculator)
        underTest.currency = currency
        underTest.ignoredProductCount = ignoreProductCount
    }

    def "should create top product" () {
        given:
            Matching matching = new Matching(matchingId: 1, topPricedCount:  2)

        when:
            TopProduct actual = underTest.process(matching)
        then:
            actual.matchingId == 1
            actual.totalPrice == 1000
            actual.avgPrice == 500
            actual.currency == currency
            actual.ignoredProductCount == ignoreProductCount
    }

    def createTransactionRepository() {
        TransactionRepository transactionRepository = Mock()
        transactionRepository.findByMatchingId(1) >> transactions1
        transactionRepository.findByMatchingId(2) >> transactions2
        return transactionRepository;
    }

    def createStatisticsCalculator() {
        TransactionStatisticsCalculator statisticsCalculator = Mock()
        statisticsCalculator.calculateStatistics(transactions1, 2) >> new DoubleSummaryStatistics(sum: 1000, count: 2)
        statisticsCalculator.calculateStatistics(transactions2, 1) >> new DoubleSummaryStatistics(sum: 200, count: 1)
        return statisticsCalculator
    }
}
