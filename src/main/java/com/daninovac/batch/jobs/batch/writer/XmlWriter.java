package com.daninovac.batch.jobs.batch.writer;

import com.daninovac.batch.jobs.entity.CsvDataDocument;
import com.daninovac.batch.jobs.entity.XmlData;
import com.daninovac.batch.jobs.repository.CsvDataRepository;
import com.daninovac.batch.jobs.repository.XmlDataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class XmlWriter implements ItemWriter<XmlData> {

  private final XmlDataRepository xmlDataRepository;

  private final CsvDataRepository csvDataRepository;

  @Override
  public void write(List<? extends XmlData> data) {
    log.info("Writing data chunk of {} from XML import to database...", data.size());
    xmlDataRepository.saveAll(data);

    //todo migrate and leave only this code
    List<CsvDataDocument> csvDataDocuments = data.stream().map(xmlData -> {
      CsvDataDocument doc = new CsvDataDocument();
      doc.setId(xmlData.getId().toString());
      doc.setFilename(xmlData.getFilename());
      doc.setType(xmlData.getType());
      doc.setProperties(xmlData.getProperties());
      return doc;
    }).collect(Collectors.toList());

    csvDataRepository.saveAll(csvDataDocuments);
  }
}
