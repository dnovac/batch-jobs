package com.daninovac.batch.jobs.service;

import com.daninovac.batch.jobs.entity.Job;
import com.daninovac.batch.jobs.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobService {

	private final JobRepository jobRepository;

	public List<Job> findAll() {
		return jobRepository.findAll();
	}

	public Job save(String name) {
		Job job = new Job(name);
		log.info("Job with name {} inserted in database", name);

		return jobRepository.save(job);
	}
}
