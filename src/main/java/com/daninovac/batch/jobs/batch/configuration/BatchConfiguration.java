package com.daninovac.batch.jobs.batch.configuration;


import com.daninovac.batch.jobs.batch.tasklet.CleanupRepositoryTasklet;
import com.daninovac.batch.jobs.batch.writer.CsvWriter;
import com.daninovac.batch.jobs.entity.ImportData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * @author Dan Novac
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;


  @Bean
  public ThreadPoolTaskExecutor jobLauncherTaskExecutor(
          @Value("${batch.max-jobs:10}") Integer maxJobs
  ) {

    ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
    threadPoolExecutor.setMaxPoolSize(maxJobs);
    threadPoolExecutor.setQueueCapacity(0);
    threadPoolExecutor.setKeepAliveSeconds(0);

    threadPoolExecutor.setRejectedExecutionHandler((r, executor) ->
            log.info("No available threads for Batch Job")
    );
    return threadPoolExecutor;
  }


  @Bean
  public Job csvImport(
          Flow importCsvFlow,
          Flow storeCsvDataFlow
  ) {

    return jobBuilderFactory.get("csvImport")
            .incrementer(new RunIdIncrementer())
            .start(importCsvFlow)
            .next(storeCsvDataFlow)
            .end()
            .build();
  }


  @Bean
  public Flow importCsvFlow(
          Step cleanupRepositoryStep,
          Step importCsvDataStep
  ) {

    return new FlowBuilder<Flow>("importCsvFlow")
            .start(cleanupRepositoryStep)
            .next(importCsvDataStep)
            .end();
  }

  @Bean
  public Step importCsvDataStep(
          FlatFileItemReader<ImportData> csvFlatItemReader,
          CsvWriter csvWriter
  ) {

    return stepBuilderFactory.get("importCsvDataStep")
            .<ImportData, ImportData>chunk(100)
            .reader(csvFlatItemReader)
            .writer(csvWriter)
            .build();
  }


  @Bean
  public Step cleanupRepositoryStep(
          StepBuilderFactory stepBuilderFactory,
          CleanupRepositoryTasklet cleanupRepositoryTasklet
  ) {

    return stepBuilderFactory.get("cleanupRepositoryStep")
            .tasklet(cleanupRepositoryTasklet)
            .build();
  }

}
