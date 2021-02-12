package com.daninovac.batch.jobs.service;

import static com.daninovac.batch.jobs.utils.Constants.DIRECTORY_NAME;
import static com.daninovac.batch.jobs.utils.Constants.TEMP_DIRECTORY;
import static com.daninovac.batch.jobs.utils.FileUtils.getFileExtension;
import static com.daninovac.batch.jobs.utils.FileUtils.getFilename;
import static com.daninovac.batch.jobs.utils.FileUtils.saveFileInTemporaryFolder;

import com.daninovac.batch.jobs.entity.XmlDataDocument;
import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.repository.XmlDataRepository;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import com.daninovac.batch.jobs.web.dto.XmlFileDataDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
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


@Slf4j
@RequiredArgsConstructor
@Service
public class XmlJobService implements IJobService{

  private final Job fileImportJob;

  private final JobLauncher jobLauncher;

  private final JobExplorer jobExplorer;

  private final XmlDataRepository repository;

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

  /**
   * @param filename
   * @return DTO with fetched data for a specific filename
   */
  public XmlFileDataDTO findAllDataByFilename(String filename) {
    log.info("Fetching all existent data from database for file: {} ...", filename);

    final List<XmlDataDocument> xmlDataByFilename = repository.findByFilename(filename);

    final List<Document> properties =
      xmlDataByFilename.stream()
        .parallel()
        .map(XmlDataDocument::getProperties)
        .collect(Collectors.toList());

    return XmlFileDataDTO.builder()
      .data(properties)
      .filename(filename)
      .type(FileTypeEnum.XML.name())
      .build();
  }


  public BatchStatus getJobStatus(Long id) {

    JobExecution jobExecution = jobExplorer.getJobExecution(id);
    if (jobExecution != null) {
      return jobExecution.getStatus();
    }
    log.warn("Job with id {} does not exist", id);
    return BatchStatus.UNKNOWN;
  }


}
