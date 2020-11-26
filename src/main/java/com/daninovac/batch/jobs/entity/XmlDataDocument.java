package com.daninovac.batch.jobs.entity;

import com.google.common.collect.ListMultimap;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class XmlDataDocument {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public String id;

  private String filename;

  private String type;

  private ListMultimap<String, Object> properties;
}
