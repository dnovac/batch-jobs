package com.daninovac.batch.jobs.batch.writer;


import com.daninovac.batch.jobs.entity.ImportData;
import com.daninovac.batch.jobs.repository.BatchJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class CsvWriter implements ItemWriter<ImportData> {

  private final BatchJobRepository batchJobRepository;

  private String filename = "defaultFilename";

  @Override
  public void write(List<? extends ImportData> list) throws Exception {

    log.info("Step write executed! Writing values in database");
    list.forEach(job -> job.setFilename(filename));

    batchJobRepository.saveAll(list);
  }

  @BeforeStep
  public void beforeStep(final StepExecution stepExecution) {
    JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
    this.filename = parameters.getString("filename");
  }
}
