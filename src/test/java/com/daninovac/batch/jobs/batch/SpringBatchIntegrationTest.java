package com.daninovac.batch.jobs.batch;

import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.TemporaryFolder;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SpringBatchIntegrationTest {

  private static final String FILE_TO_IMPORT_CSV = "fileToImportThenRemove.csv";
  private static final String TMP_FOLDER_UNIT_TEST_BATCH_JOBS = "tmpFolderUnitTestBatchJobs";

  @Rule
  public final TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Autowired
  private Job fileImportJob;

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private JobRepository jobRepository;

  private JobLauncherTestUtils jobLauncherTestUtils;

  private File tmpFolder;
  private File tmpFile;


  @BeforeEach
  public void setup() throws IOException {
    this.jobLauncherTestUtils = new JobLauncherTestUtils();
    this.jobLauncherTestUtils.setJobLauncher(jobLauncher);
    this.jobLauncherTestUtils.setJobRepository(jobRepository);
    this.jobLauncherTestUtils.setJob(fileImportJob);

    temporaryFolder.create();
    tmpFolder = temporaryFolder.newFolder(TMP_FOLDER_UNIT_TEST_BATCH_JOBS);
    tmpFile = new File(tmpFolder, FILE_TO_IMPORT_CSV);
    //todo file is not created. Maybe add text to it or change the method of creation
    System.out.println(tmpFile.exists());
  }

  private JobParameters getJobParams() {

    return new JobParametersBuilder()
      .addString(Constants.PATH_TO_UPLOADED_FILE, tmpFile.getAbsolutePath())
      .addString(Constants.PATH_TO_TEMP_FOLDER, tmpFolder.getAbsolutePath())
      .addString(Constants.DELIMITER, ";")
      .addString(Constants.FILENAME, FILE_TO_IMPORT_CSV)
      .addString(Constants.FILE_EXTENSION, FileTypeEnum.CSV.name())
      .addLong(Constants.TIME, System.currentTimeMillis())
      .toJobParameters();
  }

  @Test
  public void testMyJob() throws Exception {
    // given
    JobParameters jobParameters = getJobParams();

    // when
    JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

    // then
    Assert.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
  }

  /*@Test
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
  }*/
}
