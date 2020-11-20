package com.daninovac.batch.jobs;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableJpaRepositories
@EnableBatchProcessing
@EnableTransactionManagement
public class BatchJobsApplication {

  public static void main(String[] args) {

    SpringApplication.run(BatchJobsApplication.class, args);

    //todo maybe make it async https://medium.com/@YounessBout/increase-spring-batch-performance-through-async-processing-e96fa5d90bbd
    //todo move to no-sql
  }

}
