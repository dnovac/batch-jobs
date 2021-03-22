package com.daninovac.batch.jobs.batch;

import static org.assertj.core.api.Assertions.assertThat;

import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.repository.CsvChunkDataRepository;
import com.daninovac.batch.jobs.repository.XmlDataRepository;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.utils.FileUtils;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.TemporaryFolder;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
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

  private static final String FILE_TO_IMPORT_CSV = "fileToImportThenRemoveForUnitTests.csv";
  private static final String FILE_TO_IMPORT_XML = "fileToImportThenRemoveForUnitTests.xml";
  private static final String TMP_FOLDER_UNIT_TEST_BATCH_JOBS = "tmpFolderUnitTestBatchJobs";
  private static final String FILE_IMPORT_JOB = "fileImportJob";

  @Rule
  public final TemporaryFolder temporaryFolder = TemporaryFolder.builder()
    .assureDeletion()
    .build();

  @Autowired
  private Job fileImportJob;

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private CsvChunkDataRepository csvChunkDataRepository;

  @Autowired
  private XmlDataRepository xmlDataRepository;

  private JobLauncherTestUtils jobLauncherTestUtils;

  private File tmpFolder;
  private File tmpFile;
  private File xmlFile;


  @BeforeEach
  public void setup() throws IOException {
    this.jobLauncherTestUtils = new JobLauncherTestUtils();
    this.jobLauncherTestUtils.setJobLauncher(jobLauncher);
    this.jobLauncherTestUtils.setJobRepository(jobRepository);
    this.jobLauncherTestUtils.setJob(fileImportJob);

    createTestFolderStructure();
    writeContentToTestFile();
  }

  @AfterEach
  public void cleanup() {
    csvChunkDataRepository.deleteByFilename(FILE_TO_IMPORT_CSV);
    xmlDataRepository.deleteByFilename(FILE_TO_IMPORT_XML);

    temporaryFolder.delete();
  }

  @Test
  void givenJobParams_testFileImportJob_batchJobCompletedWithSuccessForCSV() throws Exception {
    JobParameters jobParameters = getJobParams(tmpFile);

    JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);
    JobInstance actualJobInstance = jobExecution.getJobInstance();
    ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

    assertThat(actualJobInstance.getJobName()).isEqualTo(FILE_IMPORT_JOB);
    assertThat(actualJobExitStatus.getExitCode()).isEqualTo(BatchStatus.COMPLETED.name());
    Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
  }

  @Test
  void givenJobParams_testFileImportJob_batchJobCompletedWithSuccessForXML()
    throws Exception {

    JobParameters jobParameters = getJobParams(xmlFile);

    JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

    JobInstance actualJobInstance = jobExecution.getJobInstance();
    ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

    assertThat(actualJobInstance.getJobName()).isEqualTo(FILE_IMPORT_JOB);
    assertThat(actualJobExitStatus.getExitCode()).isEqualTo(BatchStatus.COMPLETED.name());
    Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
  }

  private void createTestFolderStructure() throws IOException {
    temporaryFolder.create();
    tmpFolder = temporaryFolder.newFolder(TMP_FOLDER_UNIT_TEST_BATCH_JOBS);
    tmpFile = new File(tmpFolder, FILE_TO_IMPORT_CSV);
    xmlFile = new File(tmpFolder, FILE_TO_IMPORT_XML);
  }

  private void writeContentToTestFile() throws IOException {
    List<String> lines = Arrays.asList("TEST-The first line", ";The second line");
    List<String> linesXml = Arrays.asList("<root>TEST-The first line", "The second line</root>");

    Path file = Paths.get(tmpFile.toURI());
    Path wrongFile = Paths.get(xmlFile.toURI());

    Files.write(file, lines, StandardCharsets.UTF_8);
    Files.write(wrongFile, linesXml, StandardCharsets.UTF_8);
  }


  private JobParameters getJobParams(File tmpFile) throws InvalidFileExtensionException {

    final String filename = tmpFile.getName();
    final FileTypeEnum fileExtension = FileUtils.getFileExtension(filename);

    return new JobParametersBuilder()
      .addString(Constants.PATH_TO_UPLOADED_FILE, tmpFile.getAbsolutePath())
      .addString(Constants.PATH_TO_TEMP_FOLDER, tmpFolder.getAbsolutePath())
      .addString(Constants.DELIMITER, ";")
      .addString(Constants.FILENAME, filename)
      .addString(Constants.FILE_EXTENSION, fileExtension.name())
      .addLong(Constants.TIME, System.currentTimeMillis())
      .toJobParameters();
  }

}
