package com.daninovac.batch.jobs.batch.configuration;

import com.daninovac.batch.jobs.batch.writer.CsvWriter;
import com.daninovac.batch.jobs.entity.CsvDataDocument;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class CsvStepsConfiguration {

  private final StepBuilderFactory stepBuilderFactory;

  @Value("${batch.chunk-size:1000}")
  private int chunkSize;

  @Bean
  public Step importCsvDataStep(
      FlatFileItemReader<CsvDataDocument> csvFlatItemReader,
      AsyncItemWriter<CsvDataDocument> asyncCsvWriter,
      ThreadPoolTaskExecutor jobLauncherTaskExecutor
  ) {
    return stepBuilderFactory.get("importCsvDataStep")
        .<CsvDataDocument, Future<CsvDataDocument>>chunk(chunkSize)
        .reader(csvFlatItemReader)
        .processor(asyncProcessor(jobLauncherTaskExecutor))
        .writer(asyncCsvWriter)
        .taskExecutor(jobLauncherTaskExecutor)
        .build();
  }

  @Bean
  public AsyncItemWriter<CsvDataDocument> asyncCsvWriter(
      CsvWriter csvWriter) {
    AsyncItemWriter<CsvDataDocument> asyncItemWriter = new AsyncItemWriter<>();
    asyncItemWriter.setDelegate(csvWriter);
    return asyncItemWriter;
  }

  @Bean
  public AsyncItemProcessor<CsvDataDocument, CsvDataDocument> asyncProcessor(
      ThreadPoolTaskExecutor jobLauncherTaskExecutor
  ) {
    AsyncItemProcessor<CsvDataDocument, CsvDataDocument> asyncItemProcessor = new AsyncItemProcessor<>();
    asyncItemProcessor.setDelegate(csvItemProcessor());
    asyncItemProcessor.setTaskExecutor(jobLauncherTaskExecutor);

    return asyncItemProcessor;
  }

  @Bean
  public ItemProcessor<CsvDataDocument, CsvDataDocument> csvItemProcessor() {
    return csvDataDocument -> csvDataDocument;
  }

}
