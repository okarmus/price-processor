package org.okarmus.transaction

import org.okarmus.currency.domain.Currency
import org.okarmus.currency.repository.CurrencyRepository
import org.okarmus.transaction.domain.Transaction
import org.okarmus.transaction.repository.TransactionRepository
import org.okarmus.transaction.step.TransactionLoadStepConfiguration
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static org.mockito.Mockito.when
import static org.springframework.batch.core.BatchStatus.COMPLETED

/**
 * Created by mateusz on 14.11.16.
 */

@ActiveProfiles("batchtest")
@TestPropertySource(properties = ["dataPath = integration/dataTest.csv", "currency = PLN"])
@ContextConfiguration(classes = [TransactionJobTestConfiguration.class, TransactionLoadStepConfiguration.class])
class TransactionJobIntegrationTest extends Specification{

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils

    @Autowired
    private CurrencyRepository currencyRepository

    @Autowired
    private TransactionRepository transactionRepository

    private List<Transaction> expectedMatchingId1 = [new Transaction(id: 1, totalValue: 4500.00, quantity: 2, matchingId: 1), new Transaction(id: 2, totalValue: 525.00, quantity: 1, matchingId: 1), new Transaction(id: 3, totalValue: 2000.00, quantity: 1, matchingId: 1),]
    private List<Transaction> expectedMatchingId2 = [new Transaction(id: 4, totalValue: 7875.00, quantity: 2, matchingId: 2)]

    def "should transform data into transaction element"() {
        given:
            mockRepository()
        when:
            when(currencyRepository.findByCurrency("USD")).thenReturn(new Currency(currency: "USD", ratio: 4.5))
            JobExecution jobExecution = jobLauncherTestUtils.launchJob()
        then:
            jobExecution.getStatus() == COMPLETED
            transactionRepository.findByMatchingId(1) == expectedMatchingId1
            transactionRepository.findByMatchingId(2) == expectedMatchingId2
    }

    def mockRepository() {
        when(currencyRepository.findByCurrency("USD")).thenReturn(new Currency(currency: "USD", ratio: 4.5))
        when(currencyRepository.findByCurrency("PES")).thenReturn(new Currency(currency: "PES", ratio: 0.5))
        when(currencyRepository.findByCurrency("PLN")).thenReturn(new Currency(currency: "PLN", ratio: 1.0))
    }
}
