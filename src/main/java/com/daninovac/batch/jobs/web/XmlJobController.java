package com.daninovac.batch.jobs.web;

import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.service.XmlJobService;
import com.daninovac.batch.jobs.web.dto.ResponseBodyDTO;
import com.daninovac.batch.jobs.web.dto.XmlFileDataDTO;
import java.io.IOException;
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
  public ResponseEntity<ResponseBodyDTO> triggerImportCsvJob(
    @RequestParam MultipartFile file
  )
    throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
    IOException, JobParametersInvalidException, InvalidFileExtensionException {

    log.info("Starting job XML-Import for file: [{}] bytes", file.getSize());
    Long jobId = service.runJobXmlImport(file);

    return ResponseEntity.accepted().body(new ResponseBodyDTO(jobId));
  }

  @GetMapping("/data/{filename}")
  public XmlFileDataDTO findDataByFilename(@PathVariable String filename) {
    return service.findAllDataByFilename(filename);
  }

  @GetMapping("/status/{id}")
  public BatchStatus getJobStatus(@PathVariable("id") Long jobId) {

    return service.getJobStatus(jobId);
  }

}
