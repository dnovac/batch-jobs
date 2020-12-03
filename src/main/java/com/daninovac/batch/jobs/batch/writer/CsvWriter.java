package com.daninovac.batch.jobs.batch.writer;


import com.daninovac.batch.jobs.entity.CsvDataDocument;
import com.daninovac.batch.jobs.repository.CsvDataRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class CsvWriter implements ItemWriter<CsvDataDocument> {

  private final CsvDataRepository fileDataRepository;

  @Override
  public void write(List<? extends CsvDataDocument> data) throws Exception {

    if (data.isEmpty()) {
      log.error("List to write is empty!");
      throw new JobExecutionException("Cannot write empty list!");
    }

    log.info("Writing data chunk of {} from CSV import to database...", data.size());
    fileDataRepository.saveAll(data);
  }
}
