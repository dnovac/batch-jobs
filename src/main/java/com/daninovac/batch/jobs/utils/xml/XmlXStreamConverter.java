package com.daninovac.batch.jobs.utils.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.springframework.util.StringUtils;

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

            if(val instanceof Map) {
                marshal(val, writer, context);
            } else if (null != val) {
                writer.setValue(val.toString());
            }
            writer.endNode();
        }

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
                            UnmarshallingContext unmarshallingContext) {

        Map<String, Object> map = new HashMap<>();
        while (reader.hasMoreChildren()) {
            reader.moveDown();

            String key = reader.getNodeName();
            String value = reader.getValue().replaceAll("\\n|\\t", "");
            if (value.isBlank()) {
                map.put(key, unmarshal(reader, unmarshallingContext));
            } else {
                map.put(key, value);
            }
            reader.moveUp();
        }
        return map;
    }


}
