package com.daninovac.batch.jobs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.daninovac.batch.jobs.entity.XmlDataDocument;
import com.daninovac.batch.jobs.repository.XmlDataRepository;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import com.daninovac.batch.jobs.web.dto.XmlFileDataDTO;
import java.util.Arrays;
import java.util.Date;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;

@RunWith(MockitoJUnitRunner.class)
public class XmlJobServiceTest {


  public static final long JOB_ID = 5L;
  public static final String FILENAME_XML = "file.xml";

  @Mock
  private XmlDataRepository repository;

  @Mock
  private JobExplorer jobExplorer;

  @InjectMocks
  private XmlJobService service;


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
  public void givenFilename_findAllDataByFilename_getXmlDataFromDB() {

    XmlDataDocument dataDocument = getXmlDataDocument();

    when(repository.findByFilename(FILENAME_XML)).thenReturn(Arrays.asList(dataDocument));

    final XmlFileDataDTO xmlDataByFilename = service.findAllDataByFilename(FILENAME_XML);

    assertThat(xmlDataByFilename.getFilename()).isEqualTo(FILENAME_XML);
    assertThat(xmlDataByFilename.getType()).isEqualTo(FileTypeEnum.XML.name());
    assertThat(xmlDataByFilename.getData().get(0)).containsValue("someName");
    assertThat(xmlDataByFilename.getData().get(0)).containsValue("someTagForTheSakeOfTags");
    assertThat(xmlDataByFilename.getData().get(0)).containsKey("xmlName");
  }


  private XmlDataDocument getXmlDataDocument() {
    final Document properties = new Document();
    properties.append("xmlName", "someName");
    properties.append("xmlTag", "someTagForTheSakeOfTags");

    XmlDataDocument dataDocument = XmlDataDocument.builder()
      .filename(FILENAME_XML)
      .id("1abcd")
      .createdAt(new Date())
      .type(FileTypeEnum.XML.name())
      .properties(properties)
      .build();
    return dataDocument;
  }

}
