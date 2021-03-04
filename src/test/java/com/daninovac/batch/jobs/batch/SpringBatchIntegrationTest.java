package com.daninovac.batch.jobs.batch;

import static org.assertj.core.api.Assertions.assertThat;

import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {BatchJobsConfiguration.class, DataSourceAutoConfiguration.class})
public class SpringBatchIntegrationTest {

  private static final String FILE_TO_IMPORT_CSV = "fileToImportThenRemove.csv";
  private final JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  public SpringBatchIntegrationTest(
    Job fileImportJob,
    JobLauncher jobLauncher,
    JobRepository jobRepository) {
    this.jobLauncherTestUtils = new JobLauncherTestUtils();
    this.jobLauncherTestUtils.setJobLauncher(jobLauncher);
    this.jobLauncherTestUtils.setJobRepository(jobRepository);
    this.jobLauncherTestUtils.setJob(fileImportJob);
  }

  private JobParameters testJobParams() {

    return new JobParametersBuilder()
      .addString(Constants.PATH_TO_UPLOADED_FILE, "/yourFile/" + FILE_TO_IMPORT_CSV)
      .addString(Constants.PATH_TO_TEMP_FOLDER, "tempFolder/absolutePath")
      .addString(Constants.DELIMITER, ";")
      .addString(Constants.FILENAME, "filename")
      .addString(Constants.FILE_EXTENSION, FileTypeEnum.CSV.name())
      .addLong(Constants.TIME, System.currentTimeMillis())
      .toJobParameters();
  }

  @Test
  public void testMyJob() throws Exception {
    // given
    JobParameters jobParameters = this.jobLauncherTestUtils.getUniqueJobParameters();

    // when
    JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(testJobParams());

    // then
    Assert.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
  }

  @Test
  public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
    // given
    FileSystemResource expectedResult = new FileSystemResource("EXPECTED_OUTPUT");
    FileSystemResource actualResult = new FileSystemResource("TEST_OUTPUT");

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(testJobParams());
    JobInstance actualJobInstance = jobExecution.getJobInstance();
    ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

    // then
    assertThat(actualJobInstance.getJobName()).isEqualTo("fileImportJob");
    assertThat(actualJobExitStatus.getExitCode()).isEqualTo("COMPLETED");
    AssertFile.assertFileEquals(expectedResult, actualResult);
  }
}
