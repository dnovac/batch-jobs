package com.daninovac.batch.jobs.batch.writer;


import com.daninovac.batch.jobs.entity.FileData;
import com.daninovac.batch.jobs.repository.FileDataRepository;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
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
public class CsvWriter implements ItemWriter<FileData> {

  private final FileDataRepository fileDataRepository;

  private String filename;

  private FileTypeEnum filetype;

  @Override
  public void write(List<? extends FileData> list) throws Exception {

    log.info("Step write executed! Writing values in database");
    list.forEach(job -> {
      job.setFilename(filename);
      job.setType(filetype.name());
    });

    fileDataRepository.saveAll(list);
  }

  @BeforeStep
  public void beforeStep(final StepExecution stepExecution) {

    JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
    this.filename = parameters.getString(Constants.FILENAME);
    this.filetype = FileTypeEnum.valueOf(parameters.getString(Constants.FILE_EXTENSION));
  }

}
