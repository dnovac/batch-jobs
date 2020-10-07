package com.daninovac.batch.jobs.batch.configuration;


import com.daninovac.batch.jobs.batch.decider.ImportTypeDecider;
import com.daninovac.batch.jobs.batch.model.Student;
import com.daninovac.batch.jobs.batch.processor.XmlToCsvProcessor;
import com.daninovac.batch.jobs.batch.tasklet.CleanupRepositoryTasklet;
import com.daninovac.batch.jobs.batch.writer.CsvWriter;
import com.daninovac.batch.jobs.entity.FileData;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;


/**
 * @author Dan Novac
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Value("${batch.chunk-size:1000}")
  private int chunkSize;


  @Bean
  public ThreadPoolTaskExecutor jobLauncherTaskExecutor(
          @Value("${batch.max-jobs:10}") Integer maxJobs
  ) {

    ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
    threadPoolExecutor.setMaxPoolSize(maxJobs);
    threadPoolExecutor.setQueueCapacity(0);
    threadPoolExecutor.setKeepAliveSeconds(0);

    threadPoolExecutor.setRejectedExecutionHandler((r, executor) ->
            log.info("No available threads for Batch Job")
    );
    return threadPoolExecutor;
  }


  @Bean
  @Primary
  public Job fileImportJob(
          ImportTypeDecider fileTypeDecider,
          Step cleanupRepositoryStep,
          Flow importCsvFlow,
          Flow importXmlFlow
  ) {

    return jobBuilderFactory.get("fileImportJob")
            .incrementer(new RunIdIncrementer())
            .start(cleanupRepositoryStep)
            .next(fileTypeDecider).on(FileTypeEnum.XML.name()).to(importXmlFlow)
            .from(fileTypeDecider).on(FileTypeEnum.CSV.name()).to(importCsvFlow)
            .end()
            .build();
  }


  @Bean
  public Flow importCsvFlow(
          Step importCsvDataStep
  ) {

    return new FlowBuilder<Flow>("importCsvFlow")
            .start(importCsvDataStep)
            .end();
  }


  @Bean
  public Flow importXmlFlow(
          Step convertXmlToCsvStep,
          Step importCsvDataStep
  ) {

    return new FlowBuilder<Flow>("importXmlFlow")
            .start(convertXmlToCsvStep)
            .next(importCsvDataStep)
            .end();
  }

  @Bean
  public ImportTypeDecider fileTypeDecider() {

    return new ImportTypeDecider();
  }

  @Bean
  public Step importCsvDataStep(
          FlatFileItemReader<FileData> csvFlatItemReader,
          CsvWriter csvWriter
  ) {

    return stepBuilderFactory.get("importCsvDataStep")
            .<FileData, FileData>chunk(chunkSize)
            .reader(csvFlatItemReader)
            .writer(csvWriter)
            .build();
  }


  @Bean
  public Step cleanupRepositoryStep(
          StepBuilderFactory stepBuilderFactory,
          CleanupRepositoryTasklet cleanupRepositoryTasklet
  ) {

    return stepBuilderFactory.get("cleanupRepositoryStep")
            .tasklet(cleanupRepositoryTasklet)
            .build();
  }


  @Bean
  public Step convertXmlToCsvStep(
          XmlToCsvProcessor xmlProcessor
  ) {

    return stepBuilderFactory.get("convertXmlToCsvStep")
            .<Student, Student>chunk(chunkSize)
            .reader(reader())
            .writer(writer())
            .processor(xmlProcessor)
            .build();
  }


  @Bean
  public StaxEventItemReader<Student> reader() {

    StaxEventItemReader<Student> reader = new StaxEventItemReader<>();
    reader.setResource(new ClassPathResource("student.xml"));
    reader.setFragmentRootElementName("student");
    reader.setUnmarshaller(unMarshaller());
    return reader;
  }

  @Bean
  public FlatFileItemWriter<Student> writer() {

    FlatFileItemWriter<Student> writer = new FlatFileItemWriter<>();
    writer.setResource(new FileSystemResource("csv/student.csv"));
    writer.setLineAggregator(new DelimitedLineAggregator<Student>() {{
      setDelimiter(",");
      setFieldExtractor(new BeanWrapperFieldExtractor<Student>() {{
        setNames(new String[]{"rollNo", "name", "department"});
      }});
    }});
    return writer;
  }

  public Unmarshaller unMarshaller() {

    XStreamMarshaller unMarshal = new XStreamMarshaller();
    unMarshal.setAliases(new HashMap<String, Class>() {{
      put("student", Student.class);
    }});
    return unMarshal;
  }

}
