package com.daninovac.batch.jobs.batch.writer;

import com.daninovac.batch.jobs.entity.FileData;
import com.daninovac.batch.jobs.repository.FileDataRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class XmlWriter implements ItemWriter<FileData> {

  private final FileDataRepository fileDataRepository;

  @Override
  public void write(List<? extends FileData> data) throws Exception {
    log.info("Writing data from XML import to database...");
    fileDataRepository.saveAll(data);

  }
}
