package com.daninovac.batch.jobs.batch.writer;


import com.daninovac.batch.jobs.entity.CsvDataDocument;
import com.daninovac.batch.jobs.repository.CsvDataRepository;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class CsvWriter implements ItemWriter<CsvDataDocument> {

  private final CsvDataRepository fileDataRepository;

  private String filename;

  private FileTypeEnum filetype;

  @Override
  public void write(List<? extends CsvDataDocument> data) {

    log.info("Writing data chunk of {} from CSV import to database...", data.size());
    data.forEach(row -> {
      row.setFilename(filename);
      row.setType(filetype.name());
    });

    fileDataRepository.saveAll(data);
  }

  @BeforeStep
  public void fillParameters(final StepExecution stepExecution) {

    JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
    this.filename = parameters.getString(Constants.FILENAME);
    this.filetype = FileTypeEnum.valueOf(parameters.getString(Constants.FILE_EXTENSION));
  }

}
