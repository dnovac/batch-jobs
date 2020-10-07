package com.daninovac.batch.jobs.service;


import com.daninovac.batch.jobs.entity.FileData;
import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.repository.FileDataRepository;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.utils.FileUtils;
import com.daninovac.batch.jobs.web.dto.DataDTO;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class CsvJobService {

  //@Qualifier("csvImport")
  private final Job fileImportJob;

  private final JobLauncher jobLauncher;

  private final JobExplorer jobExplorer;

  private final FileDataRepository fileDataRepository;

  public Long runJobCsvImport(
          String delimiter,
          MultipartFile multipartFile
  ) throws IOException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
          JobParametersInvalidException, InvalidFileExtensionException {

    File file = FileUtils.saveFileInTemporaryFolder(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
    String filename = FileUtils.getFilename(multipartFile.getOriginalFilename());
    String fileExtension = FileUtils.getFileExtension(filename).name();

    JobParameters jobParameters = new JobParametersBuilder()
            .addString(Constants.PATH, file.getAbsolutePath())
            .addString(Constants.DELIMITER, delimiter)
            .addString(Constants.FILENAME, filename)
            .addString(Constants.TYPE, fileExtension)
            .addLong(Constants.TIME, System.currentTimeMillis())
            .toJobParameters();

    JobExecution jobExecution = jobLauncher.run(fileImportJob, jobParameters);

    return jobExecution.getId();
  }


  /**
   * Fetch batch job status based on job id
   *
   * @param id of the job
   * @return Batch Status [COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN]
   */
  public BatchStatus getJobStatus(Long id) {

    JobExecution jobExecution = jobExplorer.getJobExecution(id);
    if (jobExecution != null) {
      return jobExecution.getStatus();
    }
    log.warn("Job with id {} does not exist", id);
    return null;
  }

  /**
   * @param filename - uploaded file name
   * @return DataDTO
   */
  public List<DataDTO> findAllDataByFilename(String filename) {

    List<FileData> dataByFilename = fileDataRepository.findByFilename(filename);

    return dataByFilename.stream()
            .map(fileData -> DataDTO.builder()
                    .data(fileData.getProperties())
                    .build())
            .collect(Collectors.toList());
  }

  /**
   * @return list of data properties
   */
  public List<DataDTO> findAllByTypeCSV() {

    List<FileData> dataByType = fileDataRepository.findByType(FileTypeEnum.CSV.name());

    return dataByType.stream()
            .map(fileData -> DataDTO.builder()
                    .data(fileData.getProperties())
                    .filename(fileData.getFilename())
                    .build())
            .collect(Collectors.toList());
  }


}
