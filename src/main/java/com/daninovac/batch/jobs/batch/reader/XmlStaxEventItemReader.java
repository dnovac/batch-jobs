package com.daninovac.batch.jobs.batch.reader;

import com.daninovac.batch.jobs.batch.model.Student;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;


@Slf4j
@StepScope
@Component
public class XmlStaxEventItemReader extends StaxEventItemReader<Student> {


  public XmlStaxEventItemReader(@Value("#{jobParameters['path']}") String pathToFile) {
    super();
    this.setFragmentRootElementName("student");
    this.setResource(new FileSystemResource(pathToFile));
    this.setUnmarshaller(buildXStreamMarshaller());
  }

  private XStreamMarshaller buildXStreamMarshaller() {
    XStreamMarshaller marshaller = new XStreamMarshaller();

    Map<String, Class> aliasesMap = new HashMap<>();
    aliasesMap.put("student", Student.class);
    marshaller.setAliases(aliasesMap);

    return marshaller;
  }

}
