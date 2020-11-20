package com.daninovac.batch.jobs.web;

import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.service.XmlJobService;
import java.io.IOException;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
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


/**
 * @author Dan Novac on 23/09/2020
 * @project batch-jobs
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("xml")
public class XmlJobController {

  private final XmlJobService service;

  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Long> triggerImportCsvJob(
      @RequestParam @Size(max = 1) String delimiter,
      @RequestParam MultipartFile file
  )
      throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
      IOException, JobParametersInvalidException, InvalidFileExtensionException {

    log.info("Starting job XML-Import for file: [{}] bytes and delimiter [{}]", file.getSize(),
        delimiter);

    Long jobId = service.runJobXmlImport(delimiter, file);

    return ResponseEntity.accepted().body(jobId);
  }

}
