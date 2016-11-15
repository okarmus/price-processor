package org.okarmus.aggregate.step;

import org.okarmus.aggregate.domain.Matching;
import org.okarmus.aggregate.domain.TopProduct;
import org.okarmus.aggregate.header.ResultHeaderCallback;
import org.okarmus.aggregate.processor.AggregationProcessor;
import org.okarmus.utils.reader.FileReaderBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * Created by mateusz on 10.11.16.
 */

@Configuration
public class AggregateDataStepConfiguration {

    @Value("${matchingPath}")
    private String fileName;

    @Value("${resultPath}")
    private String exportFilePath;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AggregationProcessor processor;

    @Bean
    public Step aggregateData() {
        return stepBuilderFactory.get("aggregateData")
                    .<Matching, TopProduct> chunk(10)
                    .reader(matchingReader())
                    .processor(processor)
                    .writer(aggreageWriter())
                    .build();
    }

    @Bean
    public FlatFileItemReader<Matching> matchingReader() {
        String[] fields = {"matchingId", "topPricedCount"};

        BeanWrapperFieldSetMapper<Matching> fieldMapper = new BeanWrapperFieldSetMapper<>();
        fieldMapper.setTargetType(Matching.class);

        FileReaderBuilder<Matching> builder = new FileReaderBuilder<>();
        return builder.build(fileName, 1, fieldMapper ,fields);
    }

    @Bean
    public FlatFileItemWriter<TopProduct> aggreageWriter() {
        FlatFileItemWriter<TopProduct> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(exportFilePath));
        writer.setShouldDeleteIfExists(true);
        writer.setLineAggregator(lineAggregator());
        writer.setHeaderCallback(resultHeaderCallback());
        return writer;
    }

    @Bean
    public FlatFileHeaderCallback resultHeaderCallback() {
        return new ResultHeaderCallback();
    }

    @Bean
    public DelimitedLineAggregator<TopProduct> lineAggregator() {
        DelimitedLineAggregator<TopProduct> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor());
        return lineAggregator;
    }

    @Bean
    public BeanWrapperFieldExtractor<TopProduct> fieldExtractor() {
        BeanWrapperFieldExtractor<TopProduct> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"matchingId", "totalPrice", "avgPrice", "currency", "ignoredProductCount"});
        return fieldExtractor;
    }
}
