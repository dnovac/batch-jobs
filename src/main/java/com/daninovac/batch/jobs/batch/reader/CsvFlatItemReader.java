package com.daninovac.batch.jobs.batch.reader;


import com.daninovac.batch.jobs.entity.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;


@Component
@StepScope
@Slf4j
public class CsvFlatItemReader extends FlatFileItemReader<Job> {

  public CsvFlatItemReader(
          @Value("#{jobParameters['path']}") String pathToFile,
          @Value("#{jobParameters['delimiter']}") String delimiter
  ) {

    super();

    LineMapper<Job> lineMapper = createLineMapper(delimiter);

    this.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
    this.setResource(new FileSystemResource(pathToFile));
    this.setLinesToSkip(1);
    this.setLineMapper(lineMapper);
  }

  private LineMapper<Job> createLineMapper(String delimiter) {

    DefaultLineMapper<Job> lineMapper = new DefaultLineMapper<>();

    DelimitedLineTokenizer lineTokenizer = createLineTokenizer(delimiter);
    lineMapper.setLineTokenizer(lineTokenizer);

    FieldSetMapper<Job> dataMapper = createDataMapper();
    lineMapper.setFieldSetMapper(dataMapper);

    return lineMapper;
  }


  private DelimitedLineTokenizer createLineTokenizer(String delimiter) {

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(delimiter);
    lineTokenizer.setStrict(false);

    setDynamicHeaders(delimiter, lineTokenizer);

    return lineTokenizer;
  }

  private FieldSetMapper<Job> createDataMapper() {

    BeanWrapperFieldSetMapper<Job> dataMapper =
            new BeanWrapperFieldSetMapper<>();
    dataMapper.setTargetType(Job.class);

    return dataMapper;
  }

  /**
   * dynamic way of fetching headers of the csv document
   *
   * @param delimiter
   * @param lineTokenizer
   */
  private void setDynamicHeaders(String delimiter, DelimitedLineTokenizer lineTokenizer) {

    this.setSkippedLinesCallback(skippedLine -> lineTokenizer.setNames(skippedLine.split(delimiter)));
  }

}
