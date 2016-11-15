package org.okarmus.currency.service

import org.okarmus.currency.domain.Currency
import org.okarmus.currency.exception.CurrencyNotFoundException
import org.okarmus.currency.repository.CurrencyRepository
import spock.lang.Specification

/**
 * Created by mateusz on 11.11.16.
 */
class ExchangeServiceTest extends Specification {

    ExchangeService underTest
    CurrencyRepository repository

    def setup() {
        repository = createSampleRepository()
        underTest = new ExchangeService(currencyRepository: repository);
    }

    def "should exchange value properly" (src, dst, amount, expected) {
        expect:
            underTest.exchange(src, dst, amount) == expected
        where:
            src   | dst   | amount  || expected
            "EUR" | "PLN" | 1       || 2.1
            "GBP" | "EUR" | 1243    || 1420.57
            "EUR" | "GBP" | 1243    || 1087.63
    }

    def "should throw exception when dst currency is unknown"() {
        when:
            underTest.exchange("EUR", "unknown", 12.0)
        then:
            CurrencyNotFoundException ex = thrown()
            ex.message == 'Currency unknown not found'
    }

    def "should throw exception when src currency is unknown"() {
        when:
        underTest.exchange("unknown", "EUR", 12.0)
        then:
        CurrencyNotFoundException ex = thrown()
        ex.message == 'Currency unknown not found'
    }

    def createSampleRepository() {
        CurrencyRepository repository = Mock()
        repository.findByCurrency("PLN") >> new Currency(currency: "PLN", ratio: 1)
        repository.findByCurrency("EUR") >> new Currency(currency: "EUR", ratio: 2.1)
        repository.findByCurrency("GBP") >> new Currency(currency: "GBP", ratio: 2.4)

        return repository
    }


}
