package com.daninovac.batch.jobs.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dan Novac on 21/11/2020
 * @project batch-jobs
 */
@Data
@Configuration("batch")
//@ConfigurationProperties("batch")
public class BatchPropertiesConfiguration {

  private String rootName;
}
