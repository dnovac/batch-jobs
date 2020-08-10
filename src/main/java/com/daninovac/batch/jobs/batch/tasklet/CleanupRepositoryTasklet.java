package com.daninovac.batch.jobs.batch.tasklet;


import com.daninovac.batch.jobs.repository.DataRepository;
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

  private final DataRepository dataRepository;

  @Override
  public RepeatStatus execute(
          StepContribution stepContribution, ChunkContext chunkContext
  ) throws Exception {

    final JobParameters jobParameters = chunkContext.getStepContext()
            .getStepExecution()
            .getJobParameters();
    String filename = jobParameters.getString("filename");

    if (dataRepository.findByFilename(filename).size() > 0) {
      log.warn("The filename: {} has been found already in the database. Old related data will be deleted!", filename);
      dataRepository.deleteByFilename(filename);
    }
    log.info("Cleaning up database step executed");

    return RepeatStatus.FINISHED;
  }

}
