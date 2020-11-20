package com.daninovac.batch.jobs.utils.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//TODO not needed, delete it after everything OK
public class XmlMapAdapter extends XmlAdapter<XmlMapAdapter.AdaptedMap, Map<String, String>> {

  private final DocumentBuilder documentBuilder;

  public XmlMapAdapter() throws ParserConfigurationException {
    documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
  }

  @Override
  public Map<String, String> unmarshal(AdaptedMap adaptedMap) throws Exception {
    HashMap<String, String> map = new HashMap<>();
    for (Element element : adaptedMap.elements) {
      map.put(element.getLocalName(), element.getTextContent());
    }
    return map;
  }

  @Override
  public AdaptedMap marshal(Map<String, String> map) throws Exception {
    Document document = documentBuilder.newDocument();
    AdaptedMap adaptedMap = new AdaptedMap();
    for (Entry<String, String> entry : map.entrySet()) {
      Element element = document.createElement(entry.getKey());
      element.setTextContent(entry.getValue());
      adaptedMap.elements.add(element);
    }
    return adaptedMap;
  }

  public static class AdaptedMap {

    @XmlAnyElement
    public List<Element> elements = new ArrayList<>();
  }
}
