package com.daninovac.batch.jobs.utils;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XmlCustomConverter implements Converter {

  @Override
  public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter,
      MarshallingContext marshallingContext) {

  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader,
      UnmarshallingContext context) {
    String rollNo = reader.getAttribute("rollNo");
    String name = reader.getAttribute("name");
    String department = reader.getAttribute("department");
    return null;
  }

  @Override
  public boolean canConvert(Class aClass) {
    return false;
  }
}
