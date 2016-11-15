package org.okarmus;

import org.okarmus.currency.step.CurrencyLoadStepConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mateusz on 09.11.16.
 */
@Configuration
@EnableBatchProcessing
@Import(CurrencyLoadStepConfiguration.class)
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Qualifier("currencyLoad")
    private Step currencyLoad;

    @Autowired
    @Qualifier("transactionLoad")
    private Step transactionLoad;

    @Autowired
    @Qualifier("aggregateData")
    private Step aggregateData;

    @Bean
    public Job processTransactionsJob() {
        return jobBuilderFactory.get("processTransactionsJob")
                .incrementer(new RunIdIncrementer())
                .flow(currencyLoad)
                .next(transactionLoad)
                .next(aggregateData)
                .end()
                .build();
    }
}
