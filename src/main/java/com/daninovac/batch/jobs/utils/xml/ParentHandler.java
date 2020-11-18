package com.daninovac.batch.jobs.utils.xml;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class ParentHandler implements DomHandler<String, StreamResult> {

  private static final String WRAPPER_START_TAG = "<students>";
  private static final String WRAPPER_END_TAG = "</students>";

  private final StringWriter xmlWriter = new StringWriter();

  @Override
  public StreamResult createUnmarshaller(ValidationEventHandler validationEventHandler) {
    return new StreamResult(xmlWriter);
  }

  @Override
  public String getElement(StreamResult streamResult) {
    String xml = streamResult.getWriter().toString();
    int beginIndex = xml.indexOf(WRAPPER_START_TAG) + WRAPPER_START_TAG.length();
    int endIndex = xml.indexOf(WRAPPER_END_TAG);
    return xml.substring(beginIndex, endIndex);
  }

  @Override
  public Source marshal(String s, ValidationEventHandler validationEventHandler) {
    try {
      String xml = WRAPPER_START_TAG + s.trim() + WRAPPER_END_TAG;
      StringReader xmlReader = new StringReader(xml);
      return new StreamSource(xmlReader);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
