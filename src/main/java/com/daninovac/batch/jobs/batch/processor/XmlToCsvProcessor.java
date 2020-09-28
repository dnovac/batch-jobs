package com.daninovac.batch.jobs.batch.processor;

import com.daninovac.batch.jobs.batch.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class XmlToCsvProcessor implements ItemProcessor<Student, Student> {

  @Override
  public Student process(Student item) throws Exception {

    log.info("Student processed : " + item);
    return item;
  }

}
