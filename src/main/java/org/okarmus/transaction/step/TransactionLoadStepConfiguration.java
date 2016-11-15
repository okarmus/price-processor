package org.okarmus.transaction.step;

import org.okarmus.transaction.domain.Data;
import org.okarmus.transaction.domain.Transaction;
import org.okarmus.transaction.processor.DataProcessor;
import org.okarmus.utils.reader.FileReaderBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
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
public class TransactionLoadStepConfiguration {

    @Value("${dataPath}")
    private String fileName;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private DataProcessor dataProcessor;

    @Bean
    public Step transactionLoad() {
        return stepBuilderFactory.get("transactionLoad")
                .<Data,Transaction> chunk(10)
                .reader(transactionReader())
                .processor(dataProcessor)
                .writer(transactionWriter())
                .build();
    }

    @Bean
    public ItemReader<Data> transactionReader() {
        String[] fields = new String[] { "id", "price", "currency", "quantity", "matching_id"};

        BeanWrapperFieldSetMapper<Data> transactionFieldMapper = new BeanWrapperFieldSetMapper<>();
        transactionFieldMapper.setTargetType(Data.class);

        FileReaderBuilder<Data> readerBuilder = new FileReaderBuilder<>();
        return readerBuilder.build(fileName, 1, transactionFieldMapper, fields);
    }

    @Bean
    public ItemWriter<Transaction> transactionWriter() {
        JpaItemWriter<Transaction> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }
}
