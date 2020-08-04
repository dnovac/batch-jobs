package com.daninovac.batch.jobs.batch.tasklet;


import com.daninovac.batch.jobs.repository.BatchJobRepository;
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
public class CleanupRepositoryTasklet implements Tasklet {

  private final BatchJobRepository batchJobRepository;

  @Override
  public RepeatStatus execute (
      StepContribution stepContribution, ChunkContext chunkContext
  ) throws Exception {

    final JobParameters jobParameters = chunkContext.getStepContext()
        .getStepExecution()
        .getJobParameters();

    log.info("Cleaning up database step executed!");

    //todo change repo and method
    batchJobRepository.deleteById(1L);

    return RepeatStatus.FINISHED;
  }
}
