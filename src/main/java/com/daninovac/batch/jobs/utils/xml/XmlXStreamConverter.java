package com.daninovac.batch.jobs.utils.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class XmlXStreamConverter implements Converter {

  @Override
  public boolean canConvert(Class clazz) {
    return AbstractMap.class.isAssignableFrom(clazz);
  }

  @Override
  public void marshal(Object value, HierarchicalStreamWriter writer,
      MarshallingContext context) {
    AbstractMap map = (AbstractMap) value;
    for (Object object : map.entrySet()) {
      Map.Entry entry = (Map.Entry) object;
      writer.startNode(entry.getKey().toString());
      Object val = entry.getValue();
      if (val != null) {
        writer.setValue(val.toString());
      }
      writer.endNode();
    }

  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader,
      UnmarshallingContext unmarshallingContext) {

    Map<String, String> map = new HashMap<>();
    while (reader.hasMoreChildren()) {
      reader.moveDown();

      String key = reader.getNodeName();
      String value = reader.getValue();
      if (value != null && !value.isEmpty() && !value.isBlank()) {
        map.put(key, value);
      }

      reader.moveUp();
    }
    return map;
  }


}
