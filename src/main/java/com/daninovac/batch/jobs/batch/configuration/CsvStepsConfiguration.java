package com.daninovac.batch.jobs.batch.configuration;

import com.daninovac.batch.jobs.batch.writer.CsvWriter;
import com.daninovac.batch.jobs.entity.CsvDataDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CsvStepsConfiguration {

  private final StepBuilderFactory stepBuilderFactory;
  @Value("${batch.chunk-size:1000}")
  private int chunkSize;

  @Bean
  public Step importCsvDataStep(
      FlatFileItemReader<CsvDataDocument> csvFlatItemReader,
      CsvWriter csvWriter
  ) {
    return stepBuilderFactory.get("importCsvDataStep")
        .<CsvDataDocument, CsvDataDocument>chunk(chunkSize)
        .reader(csvFlatItemReader)
        .writer(csvWriter)
        .build();
  }

}
