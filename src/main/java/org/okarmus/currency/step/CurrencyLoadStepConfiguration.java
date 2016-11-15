package org.okarmus.currency.step;

import org.okarmus.currency.domain.Currency;
import org.okarmus.utils.reader.FileReaderBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

/**
 * Created by mateusz on 10.11.16.
 */

@Configuration
public class CurrencyLoadStepConfiguration {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Value("${currenciesPath}")
    private String fileName;

    @Bean
    public Step currencyLoad() {
        return stepBuilderFactory.get("currencyLoad")
                .<Currency, Currency> chunk(10)
                .reader(currencyReader())
                .writer(currencyWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<Currency> currencyReader() {
        String[] fields = new String[] {"currency", "ratio"};

        BeanWrapperFieldSetMapper<Currency> currencyFieldMapper = new BeanWrapperFieldSetMapper<>();
        currencyFieldMapper.setTargetType(Currency.class);

        FileReaderBuilder<Currency> readerBuilder = new FileReaderBuilder<>();
        return readerBuilder.build(fileName, 1, currencyFieldMapper, fields);
    }

    @Bean
    public JpaItemWriter<Currency> currencyWriter() {
        JpaItemWriter<Currency> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }
}
