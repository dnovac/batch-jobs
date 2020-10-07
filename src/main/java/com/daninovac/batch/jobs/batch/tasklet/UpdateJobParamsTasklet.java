package com.daninovac.batch.jobs.batch.tasklet;

import com.daninovac.batch.jobs.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;


/**
 * @author Dan Novac on 07/10/2020
 * @project batch-jobs
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateJobParamsTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

    chunkContext.getStepContext()
            .getStepExecution()
            .getJobExecution()
            .getExecutionContext()
            .put(Constants.PATH, Constants.XML_CONVERTED_FILENAME);

    return null;
  }

}
