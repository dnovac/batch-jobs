package com.daninovac.batch.jobs.batch.decider;

import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;


@Component
public class ImportTypeDecider implements JobExecutionDecider {


  @Override
  public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

    JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
    FileTypeEnum filetype = FileTypeEnum.valueOf(parameters.getString(Constants.TYPE));
    return new FlowExecutionStatus(filetype.name());

   /* switch (filetype) {
      case CSV -> new FlowExecutionStatus(filetype.name());
      case XML -> new FlowExecutionStatus(filetype.name());
    }*/

  }

}
