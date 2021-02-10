package com.daninovac.batch.jobs.service;


import static com.daninovac.batch.jobs.utils.Constants.DIRECTORY_NAME;
import static com.daninovac.batch.jobs.utils.Constants.TEMP_DIRECTORY;

import com.daninovac.batch.jobs.entity.CsvDataChunk;
import com.daninovac.batch.jobs.entity.CsvDataDocument;
import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.repository.CsvChunkDataRepository;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.utils.FileUtils;
import com.daninovac.batch.jobs.web.dto.CsvFileDataDTO;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CsvJobService {

  private final Job fileImportJob;

  private final JobLauncher jobLauncher;

  private final JobExplorer jobExplorer;

  private final CsvChunkDataRepository csvDataChunkRepository;

  public Long runJobCsvImport(
      String delimiter,
      MultipartFile multipartFile
  ) throws IOException,
      JobExecutionAlreadyRunningException,
      JobRestartException,
      JobInstanceAlreadyCompleteException,
      JobParametersInvalidException,
      InvalidFileExtensionException {

    File jobsTempDirectory = new File(TEMP_DIRECTORY, DIRECTORY_NAME);
    File file = FileUtils.saveFileInTemporaryFolder(jobsTempDirectory, multipartFile);
    String filename = FileUtils.getFilename(multipartFile);
    String fileExtension = FileUtils.getFileExtension(filename).name();

    JobParameters jobParameters = new JobParametersBuilder()
        .addString(Constants.PATH_TO_UPLOADED_FILE, file.getAbsolutePath())
        .addString(Constants.PATH_TO_TEMP_FOLDER, jobsTempDirectory.getAbsolutePath())
        .addString(Constants.DELIMITER, delimiter)
        .addString(Constants.FILENAME, filename)
        .addString(Constants.FILE_EXTENSION, fileExtension)
        .addLong(Constants.TIME, System.currentTimeMillis())
        .toJobParameters();

    JobExecution jobExecution = jobLauncher.run(fileImportJob, jobParameters);

    return jobExecution.getId();
  }


  /**
   * Fetch batch job status based on job id
   *
   * @param id of the job
   * @return Batch Status [COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED,
   * UNKNOWN]
   */
  public BatchStatus getJobStatus(Long id) {

    JobExecution jobExecution = jobExplorer.getJobExecution(id);
    if (jobExecution != null) {
      return jobExecution.getStatus();
    }
    log.warn("Job with id {} does not exist", id);
    return BatchStatus.UNKNOWN;
  }

  /**
   * It may cost you time, but heh...time is all we got - memories from lockdown (COVID-19)
   *
   * @param filename - uploaded file name
   * @return List of data from a specific filename
   */
  public CsvFileDataDTO findAllDataByFilename(String filename) {

    log.info("Fetching all existent data from database for file: {} ...", filename);
    List<CsvDataChunk> dataByFilenameList = csvDataChunkRepository.findByFilename(filename);

    final List<Map<String, String>> propertiesMapList =
        dataByFilenameList.stream()
            .parallel()
            .map(CsvDataChunk::getData)
            .flatMap(List::stream)
            .map(CsvDataDocument::getProperties)
            .collect(Collectors.toList());

    return CsvFileDataDTO.builder()
        .data(propertiesMapList)
        .type(FileTypeEnum.CSV.name())
        .filename(filename)
        .build();
  }


}
