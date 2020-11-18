package com.daninovac.batch.jobs.batch.reader;

import com.daninovac.batch.jobs.batch.model.Student;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.utils.xml.XmlXStreamConverter;
import com.thoughtworks.xstream.XStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;


@Slf4j
@StepScope
@Component
public class XmlReader extends FlatFileItemReader<Student> {

  private ExecutionContext executionContext;

  public XmlReader(@Value("#{jobParameters['path']}") String pathToFile) {
    super();
    this.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
    this.setResource(new FileSystemResource(pathToFile));
    this.setLinesToSkip(1);
    this.setLineMapper(new DefaultLineMapper());

   /* this.setFragmentRootElementName("student");
    this.setResource(new FileSystemResource(pathToFile));
    this.setUnmarshaller(buildXStreamMarshaller());*/
  }

  private XStreamMarshaller buildXStreamMarshaller() {
    XStreamMarshaller marshaller = new XStreamMarshaller();

    Map<String, Class> aliasesMap = new HashMap<>();
    aliasesMap.put("student", Student.class);
    marshaller.setAliases(aliasesMap);

    return marshaller;
  }

  @BeforeStep
  public void retrieveInterstepData(StepExecution stepExecution) throws JAXBException {

    JobExecution jobExecution = stepExecution.getJobExecution();
    executionContext = jobExecution.getExecutionContext();

    String path = jobExecution.getJobParameters().getString(Constants.PATH_TO_UPLOADED_FILE);

    try {
      InputStream inputStream = new FileInputStream(path);
      //String sourceXml = new String(inputStream.readAllBytes());

      String sourceXml = new BufferedReader(
          new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
          .collect(Collectors.joining("\n"));
      System.out.println(sourceXml);

      XStream xStream = new XStream();
      xStream.registerConverter(new XmlXStreamConverter());
      xStream.alias("root", Map.class);

      //it only gets different attributes, if the same it overwrites
      Map<String, String> extractedMap = (Map<String, String>) xStream.fromXML(sourceXml);
      extractedMap.values().forEach(System.out::println);
    } catch (IOException e) {
      e.printStackTrace();
    }



   /* String xml = xStream.toXML(map);
    System.out.println("Result of tweaked XStream toXml()");
    System.out.println(xml);
*/


   /* JAXBContext jc = JAXBContext.newInstance(Parent.class);
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    Parent parent = (Parent) unmarshaller.unmarshal(new File(path));

    System.out.println("Bio:   " + parent.getStudents());

    Marshaller marshaller = jc.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(parent, System.out);*/

    /*try {
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
    }*/

  }

}
