package com.daninovac.batch.jobs.entity;

import java.util.Map;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dan Novac on 25/11/2020
 * @project batch-jobs
 */
@Data
@Builder
@Document
public class CsvDataDocument {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public String id;

  private String filename;

  private String type;

  private Map<String, String> properties;
}