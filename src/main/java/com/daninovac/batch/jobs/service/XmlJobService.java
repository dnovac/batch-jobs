package com.daninovac.batch.jobs.service;

import static com.daninovac.batch.jobs.utils.Constants.DIRECTORY_NAME;
import static com.daninovac.batch.jobs.utils.Constants.TEMP_DIRECTORY;
import static com.daninovac.batch.jobs.utils.FileUtils.getFileExtension;
import static com.daninovac.batch.jobs.utils.FileUtils.getFilename;
import static com.daninovac.batch.jobs.utils.FileUtils.saveFileInTemporaryFolder;

import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.utils.Constants;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@Service
public class XmlJobService {

  private final Job fileImportJob;

  private final JobLauncher jobLauncher;

  public Long runJobXmlImport(
      MultipartFile multipartFile
  )
      throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
      JobParametersInvalidException, InvalidFileExtensionException {

    File jobsTempDirectory = new File(TEMP_DIRECTORY, DIRECTORY_NAME);
    File file = saveFileInTemporaryFolder(jobsTempDirectory, multipartFile);
    String filename = getFilename(multipartFile);
    String fileExtension = getFileExtension(filename).name();

    JobParameters jobParameters = new JobParametersBuilder()
        .addString(Constants.PATH_TO_UPLOADED_FILE, file.getAbsolutePath())
        .addString(Constants.FILENAME, filename)
        .addString(Constants.FILE_EXTENSION, fileExtension)
        .addLong(Constants.TIME, System.currentTimeMillis())
        .toJobParameters();

    JobExecution jobExecution = jobLauncher.run(fileImportJob, jobParameters);

    return jobExecution.getId();
  }


}
