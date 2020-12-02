package com.daninovac.batch.jobs.utils.xml;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.AbstractMap;
import java.util.Map;

public class XmlXStreamConverter implements Converter {

  @Override
  public boolean canConvert(Class clazz) {

    return AbstractMap.class.isAssignableFrom(clazz);
  }

  @Override
  public void marshal(Object value, HierarchicalStreamWriter writer,
      MarshallingContext context) {

    //noinspection rawtypes
    AbstractMap map = (AbstractMap) value;
    for (Object object : map.entrySet()) {
      @SuppressWarnings("rawtypes") Map.Entry entry = (Map.Entry) object;
      writer.startNode(entry.getKey().toString());
      Object val = entry.getValue();

      if (val instanceof Map) {
        marshal(val, writer, context);
      } else if (null != val) {
        writer.setValue(val.toString());
      }
      writer.endNode();
    }

  }

  @Override
  public ImmutableMultimap<String, Object> unmarshal(
      HierarchicalStreamReader reader,
      UnmarshallingContext unmarshallingContext) {

    Multimap<String, Object> map = ArrayListMultimap.create();
    while (reader.hasMoreChildren()) {
      reader.moveDown();

      String key = reader.getNodeName();
      Object value;
      if (reader.hasMoreChildren()) {
        value = unmarshal(reader, unmarshallingContext);
      } else {
        value = reader.getValue();
      }
      map.put(key, value);
      reader.moveUp();
    }
    return ImmutableMultimap.copyOf(map);
  }


}
