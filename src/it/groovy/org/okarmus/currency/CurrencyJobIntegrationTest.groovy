package org.okarmus.currency

import org.junit.Test
import org.okarmus.currency.repository.CurrencyRepository
import org.okarmus.currency.step.CurrencyLoadStepConfiguration
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static org.springframework.batch.core.BatchStatus.COMPLETED

/**
 * Created by mateusz on 13.11.16.
 */

@ActiveProfiles("batchtest")
@TestPropertySource(properties = ["currenciesPath = currency/currenciesTest.csv"])
@ContextConfiguration(classes = [CurrencyJobTestConfiguration.class, CurrencyLoadStepConfiguration.class])
class CurrencyJobIntegrationTest extends Specification {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    def "should store currency information in temporary database"() {
        when:
            JobExecution jobExecution = jobLauncherTestUtils.launchJob()
        then:
            jobExecution.getStatus() == COMPLETED
            currencyRepository.findByCurrency("USD").getRatio() == 4.5
            currencyRepository.findByCurrency("PES").getRatio() == 0.5
            currencyRepository.findByCurrency("PLN").getRatio() == 1.0
    }
}
