package org.okarmus.currency;

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

/**
 * Created by mateusz on 13.11.16.
 */
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan(basePackages = ["org.okarmus.currency"])
public class CurrencyJobTestConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Qualifier("currencyLoad")
    private Step currencyLoad;

    @Bean
    public Job testCurrencyJob() {
        return jobBuilderFactory.get("testCurrencyJob")
                .incrementer(new RunIdIncrementer())
                .flow(currencyLoad)
                .end()
                .build();
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(testCurrencyJob());
        return jobLauncherTestUtils;
    }
}
