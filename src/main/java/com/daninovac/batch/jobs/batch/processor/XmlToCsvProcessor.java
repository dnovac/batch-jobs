package com.daninovac.batch.jobs.batch.processor;

import com.daninovac.batch.jobs.entity.FileData;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class XmlToCsvProcessor implements ItemProcessor<Object, FileData> {

  @Override
  public FileData process(Object item) {

    log.info("Data processed : " + item);
    FileData fileData = new FileData();
    fileData.setFilename("filename");
    fileData.setType("XML");
    if (item instanceof Map) {
      fileData.setProperties((Map) item);
    } else {
      log.warn("Parsed XML data is not valid!");
    }
    return fileData;
  }

}
