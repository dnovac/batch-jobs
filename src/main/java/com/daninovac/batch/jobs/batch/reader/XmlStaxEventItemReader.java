package com.daninovac.batch.jobs.batch.reader;

import com.daninovac.batch.jobs.batch.model.Student;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContext;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContextFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;


@Slf4j
@StepScope
@Component
public class XmlStaxEventItemReader extends StaxEventItemReader<Student> {

  private ExecutionContext executionContext;

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

  @BeforeStep
  public void retrieveInterstepData(StepExecution stepExecution) {

    JobExecution jobExecution = stepExecution.getJobExecution();
    executionContext = jobExecution.getExecutionContext();

    String path = jobExecution.getJobParameters().getString(Constants.PATH_TO_UPLOADED_FILE);

    try {
     // https://docs.oracle.com/middleware/1212/toplink/TLJAX/dynamic_jaxb.htm#TLJAX449
      InputStream inputStream = new FileInputStream(path);
      DynamicJAXBContext dContext = DynamicJAXBContextFactory
          .createContextFromXSD(inputStream, null, null,
              null);

      DynamicEntity newCustomer = dContext.newDynamicEntity("example.Student");
      newCustomer.set("name", "George");
      newCustomer.set("lastName", "Jones");

      DynamicEntity newAddress = dContext.newDynamicEntity("example.Address");
      newAddress.set("street", "227 Main St.");
      newAddress.set("city", "Toronto");
      newAddress.set("province", "Ontario");
      newAddress.set("postalCode", "M5V1E6");

      newCustomer.set("address", newAddress);

      dContext.createMarshaller().marshal(newCustomer, System.out);

    } catch (IOException | JAXBException e) {
      log.warn("Cannot read from XML file stored at: {}", path);
      e.printStackTrace();
    }


  }

}
