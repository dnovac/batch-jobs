package com.daninovac.batch.jobs.batch.reader;


import com.daninovac.batch.jobs.entity.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
@StepScope
@Slf4j
//TODO: Job.class is just a test entity we need something to map to csv fields
public class CsvFlatItemReader extends FlatFileItemReader<Job> {

    private String[] headers;

    private int lineIndex = 0;


    public CsvFlatItemReader(
            @Value("#{jobParameters['path']}") String pathToFile,
            @Value("#{jobParameters['delimiter']}") String delimiter
    ) {

        super();

        DefaultLineMapper<Job> articleLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(delimiter);


        this.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
        this.setResource(new FileSystemResource(pathToFile));
        this.setLinesToSkip(1);
        this.setSkippedLinesCallback(s -> {

            //this.setHeaders(s.split(delimiter));

            List<String> csvHeaders = Arrays.asList(s.split(delimiter));
            lineTokenizer.setNames(this.headers);
     /* csvHeaders.forEach(header -> headerMap.put(ENUM.forProperty(header), lineIndex++));
      fieldSetMapper.setHeaderMap(headerMap);*/
        });

        this.setLineMapper(articleLineMapper);
    }
}
