package org.okarmus.transaction.processor

import org.okarmus.currency.service.ExchangeService
import org.okarmus.transaction.domain.Data
import org.okarmus.transaction.domain.Transaction
import spock.lang.Specification

/**
 * Created by mateusz on 10.11.16.
 */
class DataProcessorTest extends Specification {

    DataProcessor underTest
    ExchangeService exchangeService

    def setup() {
        exchangeService = createSampleExchangeService();
        underTest = new DataProcessor(exchangeService)
        underTest.dstCurrency="PLN"
    }

    def "should build transaction" () {
        given:
            Data data = new Data(id: 12, price: 1000, currency: "EUR", quantity: 12, matchingId: 1)
        when:
            Transaction transaction = underTest.process(data)
        then:
            transaction.id == 12
            transaction.totalValue == 2000
            transaction.quantity == 12
            transaction.matchingId == 1
    }

    def createSampleExchangeService() {
        ExchangeService exchangeService = Mock()
        exchangeService.exchange("EUR", "PLN" , 1000) >> 2000
        return exchangeService
    }
}
