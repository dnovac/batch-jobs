package com.daninovac.batch.jobs.batch.decider;

import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImportTypeDecider implements JobExecutionDecider {

  @Override
  public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

    if (stepExecution != null) {
      JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
      FileTypeEnum filetype = FileTypeEnum.valueOf(parameters.getString(Constants.FILE_EXTENSION));
      return new FlowExecutionStatus(filetype.name());
    }

    log.warn("No step executing! Job Failed!");
    return FlowExecutionStatus.FAILED;
  }
}
