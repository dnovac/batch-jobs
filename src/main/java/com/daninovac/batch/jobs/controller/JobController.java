package com.daninovac.batch.jobs.controller;

import com.daninovac.batch.jobs.entity.Job;
import com.daninovac.batch.jobs.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("job")
@RequiredArgsConstructor
public class JobController {

	private final JobService jobService;

	@GetMapping
	public List<Job> findAll() {
		return jobService.findAll();
	}

	@PostMapping("/save/{name}")
	public Job saveJob(@PathVariable String name) {
		return jobService.save(name);
	}

}
