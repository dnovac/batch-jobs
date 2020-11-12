package com.daninovac.batch.jobs.service;

import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.repository.FileDataRepository;
import com.daninovac.batch.jobs.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.io.IOException;

import static com.daninovac.batch.jobs.utils.FileUtils.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class XmlJobService {

  private final Job fileImportJob;

  private final JobLauncher jobLauncher;

  private final JobExplorer jobExplorer;

  private final FileDataRepository fileDataRepository;

  public Long runJobXmlImport(
          String delimiter,
          MultipartFile multipartFile
  ) throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
          JobParametersInvalidException, InvalidFileExtensionException {

    //File file = saveFileInTemporaryFolder(multipartFile);
    String filename = getFilename(multipartFile);
    String fileExtension = getFileExtension(filename).name();

    JobParameters jobParameters = new JobParametersBuilder()
            //.addString(Constants.PATH_TO_UPLOADED_FILE, file.getAbsolutePath())
            .addString(Constants.DELIMITER, delimiter)
            .addString(Constants.FILENAME, filename)
            .addString(Constants.FILE_EXTENSION, fileExtension)
            .addLong(Constants.TIME, System.currentTimeMillis())
            .toJobParameters();

    JobExecution jobExecution = jobLauncher.run(fileImportJob, jobParameters);

    return jobExecution.getId();
  }


}
