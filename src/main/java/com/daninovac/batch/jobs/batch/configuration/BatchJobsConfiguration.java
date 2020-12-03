package com.daninovac.batch.jobs.batch.configuration;


import com.daninovac.batch.jobs.batch.decider.ImportTypeDecider;
import com.daninovac.batch.jobs.batch.tasklet.LoggerTasklet;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
public class BatchJobsConfiguration {

  private final JobBuilderFactory jobBuilderFactory;

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
  public Job fileImportJob(
      ImportTypeDecider fileTypeDecider,
      Step loggerStep,
      Flow importCsvFlow,
      Flow importXmlFlow
  ) {
    return jobBuilderFactory.get("fileImportJob")
        .incrementer(new RunIdIncrementer())
        .start(loggerStep)
        .next(fileTypeDecider).on(FileTypeEnum.XML.name()).to(importXmlFlow)
        .from(fileTypeDecider).on(FileTypeEnum.CSV.name()).to(importCsvFlow)
        .end()
        .build();
  }

  @Bean
  public Flow importCsvFlow(
      Step importCsvDataStep
  ) {
    return new FlowBuilder<Flow>("importCsvFlow")
        .start(importCsvDataStep)
        .end();
  }

  @Bean
  public Flow importXmlFlow(
      Step importXmlDataStep
  ) {
    return new FlowBuilder<Flow>("importXmlFlow")
        .start(importXmlDataStep)
        .end();
  }

  @Bean
  public ImportTypeDecider fileTypeDecider() {
    return new ImportTypeDecider();
  }

  //todo just to start, should find another solution
  @Bean
  public Step loggerStep(
      StepBuilderFactory stepBuilderFactory,
      LoggerTasklet loggerTasklet
  ) {
    return stepBuilderFactory.get("loggerStep")
        .tasklet(loggerTasklet)
        .build();
  }

}
