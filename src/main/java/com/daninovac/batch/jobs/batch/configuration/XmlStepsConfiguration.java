package com.daninovac.batch.jobs.batch.configuration;

import com.daninovac.batch.jobs.batch.model.Student;
import com.daninovac.batch.jobs.batch.tasklet.UpdateJobParamsTasklet;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class XmlStepsConfiguration {

  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Step updateJobParamsStep(
      StepBuilderFactory stepBuilderFactory,
      UpdateJobParamsTasklet updateJobParamsTasklet
  ) {
    return stepBuilderFactory.get("updateJobParamsStep")
        .tasklet(updateJobParamsTasklet)
        .build();
  }


  /*@Bean
  public StaxEventItemReader<Student> xmlReader() {
    StaxEventItemReader<Student> reader = new StaxEventItemReader<>();
    reader.setResource(new ClassPathResource("test-data/student.xml"));
    reader.setFragmentRootElementName("student");
    reader.setUnmarshaller(xmlUnMarshaller());
    return reader;
  }*/

  /*@Bean
  public FlatFileItemWriter<Student> xmlWriter() {
    FlatFileItemWriter<Student> writer = new FlatFileItemWriter<>();
    String path = Constants.TEMP_DIRECTORY + "/" + Constants.DIRECTORY_NAME + "/"
        + Constants.XML_CONVERTED_FILENAME;
    writer.setResource(new FileSystemResource(path));
    writer.setLineAggregator(new DelimitedLineAggregator<Student>() {{
      setDelimiter(";");
      setFieldExtractor(new BeanWrapperFieldExtractor<Student>() {{
        setNames(new String[]{"rollNo", "name", "department"});
      }});
    }});
    return writer;
  }*/

  public Unmarshaller xmlUnMarshaller() {
    XStreamMarshaller unMarshal = new XStreamMarshaller();
    unMarshal.setAliases(new HashMap<String, Class>() {{
      put("student", Student.class);
    }});
    return unMarshal;
  }
}
