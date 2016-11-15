package org.okarmus.utils.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by mateusz on 10.11.16.
 */
public class FileReaderBuilder<T> {

    public FlatFileItemReader<T> build(String resourceName, int skipLines, BeanWrapperFieldSetMapper<T> fieldMapper, String... fields){
        FlatFileItemReader<T> reader = new FlatFileItemReader<>();
        reader.setResource(resource(resourceName));
        reader.setLinesToSkip(skipLines);
        reader.setLineMapper(lineMapper(fields, fieldMapper));
        return reader;
    }

    private LineMapper<T> lineMapper(String[] fields, BeanWrapperFieldSetMapper<T> fieldMapper) {
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();

        lineMapper.setFieldSetMapper(fieldMapper);

        DelimitedLineTokenizer lineTokenizer = lineTokenizer(fields);
        lineMapper.setLineTokenizer(lineTokenizer);

        return lineMapper;
    }

    private DelimitedLineTokenizer lineTokenizer(String[] fields) {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(fields);
        return lineTokenizer;
    }

    private Resource resource(String resourceName) {
        return new ClassPathResource(resourceName);
    }
}
