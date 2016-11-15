package org.okarmus.transaction;

import org.okarmus.currency.domain.Currency;
import org.okarmus.currency.repository.CurrencyRepository;
import org.okarmus.currency.service.ExchangeService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mateusz on 14.11.16.
 */
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan(basePackages = ["org.okarmus.transaction"])
public class TransactionJobTestConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Qualifier("transactionLoad")
    private Step transactionLoad;

    @Bean
    public Job testCurrencyJob() {
        return jobBuilderFactory.get("testTransactionJob")
                .incrementer(new RunIdIncrementer())
                .flow(transactionLoad)
                .end()
                .build();
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(testCurrencyJob());
        return jobLauncherTestUtils;
    }

    @Bean
    public ExchangeService exchangeService() {
        return new ExchangeService();
    }

    @Bean
    private CurrencyRepository currencyRepository() {
        return mock(CurrencyRepository.class);
    }
}
