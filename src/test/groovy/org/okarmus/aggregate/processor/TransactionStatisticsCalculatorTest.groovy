package org.okarmus.aggregate.processor

import org.okarmus.transaction.domain.Transaction
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by mateusz on 11.11.16.
 */
class TransactionStatisticsCalculatorTest extends Specification {

    @Shared
    List<Transaction> transactions1 = [new Transaction(1, 1000, 2, 1), new Transaction(3, 150, 10, 1), new Transaction(5, 1600, 1, 1)]
    @Shared
    List<Transaction> transactions2 = [new Transaction(2, 300, 2, 2), new Transaction(4, 600, 3, 2), new Transaction(4, 600, 5, 2), new Transaction(4, 600, 4, 2)]

    def "should take product count under consideration"(transactions, count, sum, average) {
        expect:
            TransactionStatisticsCalculator underTest = new TransactionStatisticsCalculator(ignoredProductCount: false)
            DoubleSummaryStatistics statistics = underTest.calculateStatistics(transactions, count)
            statistics.sum == sum
            statistics.average == average
        where:
            transactions |count ||sum  | average
            transactions1|2     ||3600 | 1200
            transactions2|2     ||5400 | 600
            transactions2|3     ||7200 | 600
    }

    def "should ignore product count"(transactions, count, sum, average) {
        expect:
        TransactionStatisticsCalculator underTest = new TransactionStatisticsCalculator(ignoredProductCount: true)
        DoubleSummaryStatistics statistics = underTest.calculateStatistics(transactions, count)
        statistics.sum == sum
        statistics.average == average
        where:
        transactions |count ||sum  | average
        transactions1|2     ||2600 | 1300
        transactions2|2     ||1200 | 600
        transactions2|3     ||1800 | 600
    }


}
