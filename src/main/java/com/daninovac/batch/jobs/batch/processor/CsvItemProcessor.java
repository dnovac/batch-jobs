package com.daninovac.batch.jobs.batch.processor;

import com.daninovac.batch.jobs.entity.CsvDataDocument;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class CsvItemProcessor implements ItemProcessor<CsvDataDocument, CsvDataDocument> {

  private final String filename;
  private final String fileType;

  public CsvItemProcessor(
      @Value("#{jobParameters['filename']}") String filename,
      @Value("#{jobParameters['file_extension']}") String fileType) {
    this.filename = filename;
    this.fileType = FileTypeEnum.valueOf(fileType).name();
  }

  @Override
  public CsvDataDocument process(CsvDataDocument csvDataDocument) {
    csvDataDocument.setFilename(filename);
    csvDataDocument.setType(fileType);
    return csvDataDocument;
  }

}
