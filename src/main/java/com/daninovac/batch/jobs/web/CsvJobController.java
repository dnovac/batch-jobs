package com.daninovac.batch.jobs.web;


import com.daninovac.batch.jobs.service.CsvJobService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.io.IOException;


@Data
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("csv")
public class CsvJobController {

  private final CsvJobService csvJobService;


  @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Long> triggerImportCsvJob(
          @RequestParam(required = true) @Size(max = 1) String delimiter,
          @RequestParam(required = true) MultipartFile file
  ) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException {

    log.info("Starting job CSV-Import for file: [{}] bytes and delimiter [{}]", file.getSize(), delimiter);

    Long jobId = csvJobService.runJobCsvImport(delimiter, file);

    return ResponseEntity.accepted().body(jobId);
  }


}
