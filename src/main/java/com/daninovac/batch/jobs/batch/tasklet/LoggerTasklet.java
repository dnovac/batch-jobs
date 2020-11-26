package com.daninovac.batch.jobs.batch.tasklet;


import com.daninovac.batch.jobs.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoggerTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(
      StepContribution stepContribution, ChunkContext chunkContext) {

    final JobParameters jobParameters = chunkContext.getStepContext()
        .getStepExecution()
        .getJobParameters();
    String filename = jobParameters.getString(Constants.FILENAME);
    log.info(String.format("Starting to import file %s...", filename));

    return RepeatStatus.FINISHED;
  }
}
