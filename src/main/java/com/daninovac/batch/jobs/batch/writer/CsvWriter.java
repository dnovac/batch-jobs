package com.daninovac.batch.jobs.batch.writer;


import com.daninovac.batch.jobs.entity.Job;
import com.daninovac.batch.jobs.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class CsvWriter implements ItemWriter<Job> {

  private final JobRepository jobRepository;


  @Override
  public void write (List<? extends Job> list) throws Exception {

    log.info("Step write executed! Writing values in database");
    jobRepository.saveAll(list);
  }
}
