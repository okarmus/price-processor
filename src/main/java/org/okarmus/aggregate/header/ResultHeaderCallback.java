package org.okarmus.aggregate.header;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by mateusz on 12.11.16.
 */
public class ResultHeaderCallback implements FlatFileHeaderCallback {

    @Value("${header}")
    private String header;

    @Override
    public void writeHeader(Writer writer) throws IOException {
        writer.write(header);
    }
}
