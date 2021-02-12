package com.daninovac.batch.jobs.service;

import org.springframework.batch.core.BatchStatus;

public interface IJobService {

  /**
   * @param id of the job to search for
   * @return COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN;
   */
  BatchStatus getJobStatus(Long id);

}
