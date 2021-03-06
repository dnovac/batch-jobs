package com.daninovac.batch.jobs.batch.configuration;

import com.daninovac.batch.jobs.batch.processor.XmlToCsvProcessor;
import com.daninovac.batch.jobs.batch.reader.xml.XmlReader;
import com.daninovac.batch.jobs.batch.writer.XmlWriter;
import com.daninovac.batch.jobs.entity.XmlDataDocument;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
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
        .<Object, XmlDataDocument>chunk(chunkSize)
        .reader(xmlReader)
        .writer(xmlWriter)
        .processor(xmlProcessor)
        .build();
  }

}
