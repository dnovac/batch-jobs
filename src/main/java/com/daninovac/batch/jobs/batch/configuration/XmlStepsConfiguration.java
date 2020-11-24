package com.daninovac.batch.jobs.batch.configuration;

import com.daninovac.batch.jobs.batch.processor.XmlToCsvProcessor;
import com.daninovac.batch.jobs.batch.reader.xml.XmlReader;
import com.daninovac.batch.jobs.batch.writer.XmlWriter;
import com.daninovac.batch.jobs.entity.FileData;
import com.daninovac.batch.jobs.entity.XmlData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class XmlStepsConfiguration {

  @Value("${batch.chunk-size:1000}")
  private int chunkSize;

  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Step importXmlDataStep(
      XmlReader xmlReader,
      XmlWriter xmlWriter,
      XmlToCsvProcessor xmlProcessor
  ) {
    return stepBuilderFactory.get("importXmlDataStep")
        .<Object, XmlData>chunk(chunkSize)
        .reader(xmlReader)
        .writer(xmlWriter)
        .processor(xmlProcessor)
        .build();
  }

}
