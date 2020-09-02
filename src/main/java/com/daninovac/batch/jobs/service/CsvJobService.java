package com.daninovac.batch.jobs.service;


import com.daninovac.batch.jobs.entity.FileData;
import com.daninovac.batch.jobs.repository.DataRepository;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CsvJobService {

  private final Job job;

  private final JobLauncher jobLauncher;

  private final JobExplorer jobExplorer;

  private final DataRepository dataRepository;

  public Long runJobCsvImport(
          String delimiter,
          MultipartFile multipartFile
  ) throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {

    File file = saveFileInTemporaryFolder(multipartFile);

    String filename = getFilename(multipartFile);
    JobParameters jobParameters = new JobParametersBuilder()
            .addString(Constants.PATH, file.getAbsolutePath())
            .addString(Constants.DELIMITER, delimiter)
            .addString(Constants.FILENAME, filename)
            .addString(Constants.TYPE, getFileExtension(filename).name())
            .addLong(Constants.TIME, System.currentTimeMillis())
            .toJobParameters();

    JobExecution jobExecution = jobLauncher.run(job, jobParameters);

    return jobExecution.getId();
  }


  private File saveFileInTemporaryFolder(MultipartFile multipartFile) throws IOException {

    File tempUploadedFileDirectory = new File(Constants.TEMP_DIRECTORY, "jobs");

    if (!tempUploadedFileDirectory.exists()) {
      tempUploadedFileDirectory.mkdir();
    }

    if (tempUploadedFileDirectory.exists()) {
      final String originalFilename = getFilename(multipartFile);
      File fileToImport = new File(tempUploadedFileDirectory, originalFilename);

      try (OutputStream outputStream = new FileOutputStream(fileToImport)) {
        IOUtils.copy(multipartFile.getInputStream(), outputStream);
        log.info("Saving csv of size {} in temporary folder", multipartFile.getSize());
        outputStream.flush();
      }
      return fileToImport;
    }
    throw new FileNotFoundException();
  }

  /**
   * Fetch batch job status based on job id
   *
   * @param id of the job
   * @return Batch Status [COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN;]
   */
  public BatchStatus getJobStatus(Long id) {

    JobExecution jobExecution = jobExplorer.getJobExecution(id);
    if (jobExecution != null) {
      return jobExecution.getStatus();
    }
    log.warn("Job with id {} does not exist", id);
    return null;
  }

  public List<FileData> findAllByFilename(String filename) {

    return dataRepository.findByFilename(filename);
  }

  public FileTypeEnum getFileExtension(String filename) {

    String fileExtension = Files.getFileExtension(filename);

    if (fileExtension.isEmpty()) {
      log.error("File has no extension!");
      throw new NoSuchElementException(); //todo create new custom exc
    }

    return FileTypeEnum.valueOf(fileExtension.toUpperCase());
  }

  private String getFilename(MultipartFile multipartFile) {

    return Optional.ofNullable(multipartFile.getOriginalFilename()).orElse(Constants.DEFAULT_FILENAME);
  }

}
