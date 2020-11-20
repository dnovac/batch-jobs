package com.daninovac.batch.jobs.batch.reader.xml;

import com.daninovac.batch.jobs.utils.xml.XmlXStreamConverter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;


@Slf4j
@StepScope
@Component
public class XmlReader extends StaxEventItemReader<Object> {

  public XmlReader(@Value("#{jobParameters['path']}") String path) {
    XStreamMarshaller unMarshaller = new XStreamMarshaller();
    Map<String, Class<?>> aliases = new HashMap<>();
    aliases.put("root", Map.class);

    unMarshaller.setConverters(new XmlXStreamConverter());
    unMarshaller.setAliases(aliases);

    this.setResource(new PathResource(path));
    this.setFragmentRootElementName("root");
    this.setUnmarshaller(unMarshaller);
  }
}
