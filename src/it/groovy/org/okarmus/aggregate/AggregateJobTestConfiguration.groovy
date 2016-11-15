package org.okarmus.aggregate;

import org.okarmus.transaction.repository.TransactionRepository;
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

import static org.mockito.Mockito.mock;

/**
 * Created by mateusz on 14.11.16.
 */
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan(basePackages = ["org.okarmus.aggregate"])
public class AggregateJobTestConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Qualifier("aggregateData")
    private Step aggregateData;

    @Bean
    public Job testCurrencyJob() {
        return jobBuilderFactory.get("testAggregateJob")
                .incrementer(new RunIdIncrementer())
                .flow(aggregateData)
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
    public TransactionRepository transactionRepository() {
        return mock(TransactionRepository.class);
    }
}
