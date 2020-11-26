package com.daninovac.batch.jobs.batch.writer;

import com.daninovac.batch.jobs.entity.XmlDataDocument;
import com.daninovac.batch.jobs.repository.XmlDataRepository;
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
public class XmlWriter implements ItemWriter<XmlDataDocument> {

  private final XmlDataRepository xmlDataRepository;

  @Override
  public void write(List<? extends XmlDataDocument> data) {
    log.info("Writing data chunk of {} from XML import to database...", data.size());

    xmlDataRepository.saveAll(data);
  }
}
