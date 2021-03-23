package com.daninovac.batch.jobs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.daninovac.batch.jobs.entity.CsvDataChunk;
import com.daninovac.batch.jobs.entity.CsvDataDocument;
import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.repository.CsvChunkDataRepository;
import com.daninovac.batch.jobs.web.dto.CsvFileDataDTO;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class CsvJobServiceTest {

  public static final long JOB_ID = 5L;
  public static final String FILENAME_CSV = "filename.csv";

  @Mock
  private CsvChunkDataRepository repository;

  @Mock
  private JobExplorer jobExplorer;

  @Mock
  private JobLauncher jobLauncher;

  @InjectMocks
  private CsvJobService service;


  @Test
  public void whenRunCsvJob_thenReturnLaunchedJobId()
    throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException, InvalidFileExtensionException {

    MockMultipartFile mockMultipartFile = new MockMultipartFile(
      "file",
      "file.csv",
      MediaType.MULTIPART_FORM_DATA_VALUE,
      "Testing;For;A;Better;World".getBytes()
    );

    final JobExecution jobExecution = new JobExecution(JOB_ID);
    when(jobLauncher.run(any(), any())).thenReturn(jobExecution);

    final Long jobId = service.runJobCsvImport("_", mockMultipartFile);
    assertThat(jobId).isEqualTo(JOB_ID);
  }

  @Test
  public void givenJobId_whenGetJobStatus_returnBatchJobStatus() {

    final JobExecution job = new JobExecution(JOB_ID);
    job.setStatus(BatchStatus.STARTING);

    when(jobExplorer.getJobExecution(JOB_ID)).thenReturn(job);

    BatchStatus status = service.getJobStatus(JOB_ID);

    assertThat(status).isEqualTo(BatchStatus.STARTING);
  }

  @Test
  public void givenJobId_whenGetJobStatus_returnDefaultBatchJobStatusUnknown() {

    when(jobExplorer.getJobExecution(JOB_ID)).thenReturn(null);

    BatchStatus status = service.getJobStatus(JOB_ID);

    assertThat(status).isEqualTo(BatchStatus.UNKNOWN);
  }

  @Test
  public void givenFilename_findAllDataByFilename_getCsvDataFromDB() {

    final CsvDataChunk csvDataChunk = buildCsvDataChunk();
    when(repository.findByFilename(FILENAME_CSV)).thenReturn(Arrays.asList(csvDataChunk));

    final CsvFileDataDTO allDataByFilename = service.findAllDataByFilename(FILENAME_CSV);

    assertThat(allDataByFilename.getFilename()).isEqualTo(FILENAME_CSV);
    assertThat(allDataByFilename.getType()).isEqualTo(FileTypeEnum.CSV.name());
    assertThat(allDataByFilename.getData().size()).isEqualTo(2);
    assertThat(allDataByFilename.getData().get(0)).containsKey("name");
    assertThat(allDataByFilename.getData().get(0)).containsKey("title");
    assertThat(allDataByFilename.getData().get(0)).containsKey("another_data");
    assertThat(allDataByFilename.getData().get(1)).containsKey("another_data");
    assertThat(allDataByFilename.getData().get(0)).containsValue("justTests");
  }


  private CsvDataChunk buildCsvDataChunk() {
    final Date now = new Date();
    Map<String, String> dataMap = new HashMap<String, String>() {{
      put("name", "testingStuff");
      put("title", "justTests");
      put("another_data", "anotherTime");
    }};
    CsvDataDocument dataDocument = CsvDataDocument.builder()
      .properties(dataMap)
      .build();
    CsvDataDocument dataDocument1 = CsvDataDocument.builder()
      .properties(dataMap)
      .build();
    return CsvDataChunk.builder()
      .filename(FILENAME_CSV)
      .id("abc458593lsd")
      .type(FileTypeEnum.CSV.name())
      .createdAt(now)
      .data(Arrays.asList(dataDocument, dataDocument1))
      .build();
  }


}
