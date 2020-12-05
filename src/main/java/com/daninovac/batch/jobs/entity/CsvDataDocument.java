package com.daninovac.batch.jobs.entity;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dan Novac on 25/11/2020
 * @project batch-jobs
 */
@Data
@Builder
@Document
public class CsvDataDocument {

  private Map<String, String> properties;


}
