package com.daninovac.batch.jobs.repository;

import com.daninovac.batch.jobs.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobRepository extends JpaRepository<Job, Long> {
}
