package com.daninovac.batch.jobs.web;


import com.daninovac.batch.jobs.entity.FileData;
import com.daninovac.batch.jobs.service.CsvJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("csv")
public class CsvJobController {

  private final CsvJobService csvJobService;


  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Long> triggerImportCsvJob(
          @RequestParam(required = true) @Size(max = 1) String delimiter,
          @RequestParam(required = true) MultipartFile file
  ) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException, JobParametersInvalidException {

    log.info("Starting job CSV-Import for file: [{}] bytes and delimiter [{}]", file.getSize(), delimiter);

    Long jobId = csvJobService.runJobCsvImport(delimiter, file);

    return ResponseEntity.accepted().body(jobId);
  }

  @GetMapping("/find/{filename}")
  public List<FileData> findByFilename(@PathVariable String filename) {

    return csvJobService.findAllByFilename(filename);
  }

  @GetMapping("/status/{id}")
  public BatchStatus getJobStatus(@PathVariable("id") Long jobId) {

    return csvJobService.getJobStatus(jobId);
  }


}
