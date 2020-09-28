package com.daninovac.batch.jobs.batch.reader;
/*
import com.daninovac.batch.jobs.batch.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import java.util.HashMap;


@Component
@Slf4j
public class XmlStaxEventItemReader extends StaxEventItemReader<Student> {


  public XmlStaxEventItemReader() {

    super.setFragmentRootElementName("student");
    super.setResource(new ClassPathResource("student.xml"));

    XStreamMarshaller unMarshal = new XStreamMarshaller();
    unMarshal.setAliases(new HashMap<String, Class>() {{
      put("student", Student.class);
    }});
    super.setUnmarshaller(unMarshal);
  }

}*/
