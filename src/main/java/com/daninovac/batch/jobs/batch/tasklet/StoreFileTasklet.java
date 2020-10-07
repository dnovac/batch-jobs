package com.daninovac.batch.jobs.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
@StepScope
public class StoreFileTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

    return null;
  }

 /* @BeforeStep
  public void beforeStep(final StepExecution stepExecution) {

    JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
    this.filename = parameters.getString(Constants.FILENAME);
    this.filetype = FileTypeEnum.valueOf(parameters.getString(Constants.TYPE));
  }*/

}
