package com.daninovac.batch.jobs.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CsvJobService {

  private static final File TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));
  private static final String JOB_NAME_IMPORT_CSV = "csvImport";
  private static final String DELIMITER = "delimiter";
  private static final String PATH = "path";
  private static final String TIME = "time";
  private static final String FILENAME = "filename";
  private static final String DEFAULT_FILENAME = "csv_default";

  private final Job csvImport;

  private final JobLauncher jobLauncher;

  //private final JobExplorer jobExplorer;

  public Long runJobCsvImport(
          String delimiter,
          MultipartFile multipartFile
  ) throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {

    File file = saveFileInTemporaryFolder(multipartFile);

    JobParameters jobParameters = new JobParametersBuilder()
            .addString(PATH, file.getAbsolutePath())
            .addString(DELIMITER, delimiter)
            .addString(FILENAME, multipartFile.getName())
            .addLong(TIME, System.currentTimeMillis())
            .toJobParameters();

    JobExecution jobExecution = jobLauncher.run(csvImport, jobParameters);

    return jobExecution.getId();
  }


  private File saveFileInTemporaryFolder(MultipartFile multipartFile) throws IOException {

    File tempUploadedFileDirectory = new File(TEMP_DIRECTORY, "jobs");

    if (!tempUploadedFileDirectory.exists()) {
      tempUploadedFileDirectory.mkdir();
    }

    if (tempUploadedFileDirectory.exists()) {
      final String originalFilename = Optional.of(multipartFile.getOriginalFilename()).orElse(DEFAULT_FILENAME);
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

}
