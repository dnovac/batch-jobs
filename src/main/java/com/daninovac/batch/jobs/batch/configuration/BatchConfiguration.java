package com.daninovac.batch.jobs.batch.configuration;


import com.daninovac.batch.jobs.batch.tasklet.CleanupRepositoryTasklet;
import com.daninovac.batch.jobs.batch.writer.CsvWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfiguration {

  private final BeanFactory beanFactory;

  private final JobBuilderFactory jobBuilderFactory;


  @Bean
  public ThreadPoolTaskExecutor jobLauncherTaskExecutor(
          @Value("${batch.max-jobs:10}") Integer maxJobs
  ) {

    ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
    threadPoolExecutor.setMaxPoolSize(maxJobs);
    threadPoolExecutor.setQueueCapacity(0);
    threadPoolExecutor.setKeepAliveSeconds(0);

    threadPoolExecutor.setRejectedExecutionHandler((r, executor) -> log.info(
            "Batch Job execution rejected, no available threads."));
    return threadPoolExecutor;
  }


  @Bean
  public JobLauncher asyncJobLauncher(
          JobRepository jobRepository,
          ThreadPoolTaskExecutor jobLauncherTaskExecutor
  ) throws Exception {

    SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
    simpleJobLauncher.setJobRepository(jobRepository);
    /*simpleJobLauncher.setTaskExecutor(TaskExecutor(
        beanFactory,
        jobLauncherTaskExecutor
    ));*/
    simpleJobLauncher.afterPropertiesSet();
    return simpleJobLauncher;
  }


  @Bean
  public Job csvImport(
          JobBuilderFactory jobBuilderFactory,
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
  public Step cleanupRepositoryStep(
          StepBuilderFactory stepBuilderFactory,
          CleanupRepositoryTasklet cleanupRepositoryTasklet
  ) {

    return stepBuilderFactory.get("cleanupRepositoryStep")
            .tasklet(cleanupRepositoryTasklet)
            .build();
  }


  @Bean
  public Step importCsvDataStep(
          StepBuilderFactory stepBuilderFactory,
          FlatFileItemReader<com.daninovac.batch.jobs.entity.Job> csvFlatItemReader,
          //Processor processor,
          CsvWriter csvWriter
  ) {

    return stepBuilderFactory.get("importCsvDataStep")
            .<com.daninovac.batch.jobs.entity.Job, com.daninovac.batch.jobs.entity.Job>chunk(100)
            .reader(csvFlatItemReader)
            //.processor(null)
            .writer(csvWriter)
            .build();
  }

}
