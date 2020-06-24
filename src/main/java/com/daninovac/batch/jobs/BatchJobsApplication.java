package com.daninovac.batch.jobs;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
@EnableBatchProcessing
public class BatchJobsApplication {

  public static void main (String[] args) {

    SpringApplication.run(BatchJobsApplication.class, args);
  }

}
