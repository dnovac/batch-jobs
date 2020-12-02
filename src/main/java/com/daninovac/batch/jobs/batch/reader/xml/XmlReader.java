package com.daninovac.batch.jobs.batch.reader.xml;

import com.daninovac.batch.jobs.utils.xml.XmlXStreamConverter;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;


@Slf4j
@Getter
@Setter
@StepScope
@Component
public class XmlReader extends StaxEventItemReader<Object> {

  public XmlReader(@Value("#{jobParameters['path']}") String path,
      @Value("${batch.root-name:root}") String rootPropertyName)
      throws JobParametersInvalidException {

    if (rootPropertyName == null || Strings.isEmpty(rootPropertyName)) {
      log.error("XML root property name is not defined!");
      throw new JobParametersInvalidException(
          "XML root property must be defined in the properties file!");
    }

    XStreamMarshaller unMarshaller = new XStreamMarshaller();
    Map<String, Class<?>> aliases = new HashMap<>();
    aliases.put(rootPropertyName, Map.class);

    unMarshaller.setConverters(new XmlXStreamConverter());
    unMarshaller.setAliases(aliases);

    this.setResource(new PathResource(path));
    this.setFragmentRootElementName(rootPropertyName);
    this.setUnmarshaller(unMarshaller);
  }
}
