package com.daninovac.batch.jobs.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.daninovac.batch.jobs.service.XmlJobService;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import com.daninovac.batch.jobs.web.dto.XmlFileDataDTO;
import java.util.Arrays;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(XmlJobController.class)
public class XmlJobControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private XmlJobService service;

  @Test
  public void givenValidFile_whenFileUploaded_thenReturnStatus202WithJobId()
    throws Exception {

    MockMultipartFile mockMultipartFile = new MockMultipartFile(
      "file",
      "file.xml",
      MediaType.MULTIPART_FORM_DATA_VALUE,
      "<tag>something</tag><anotherTag>hello</anotherTag>".getBytes()
    );

    given(service.runJobXmlImport(mockMultipartFile)).willReturn(999L);

    mockMvc.perform(multipart("/xml/import")
      .file(mockMultipartFile))
      .andExpect(status().is2xxSuccessful())
      .andExpect(status().is(202))
      .andExpect(jsonPath("$.jobId").exists())
      .andExpect(jsonPath("$.jobId").isNumber())
      .andExpect(jsonPath("$.jobId").value(999L));
  }

  @Test
  public void givenFilename_whenFindDataByFilename_thenReturnStatus200WithContent()
    throws Exception {

    final String filename = "test_xml_filename.xml";
    Document dataDoc = new Document();
    dataDoc.append("tag", "something");
    dataDoc.append("anotherTag", "hello");

    XmlFileDataDTO xmlData = XmlFileDataDTO.builder()
      .filename(filename)
      .type(FileTypeEnum.XML.name())
      .data(Arrays.asList(dataDoc))
      .build();

    given(service.findAllDataByFilename(filename)).willReturn(xmlData);

    mockMvc.perform(get("/xml/data/" + filename))
      .andExpect(status().is2xxSuccessful())
      .andExpect(status().is(200))
      .andExpect(jsonPath("$.filename").exists())
      .andExpect(jsonPath("$.type").exists())
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.filename").value(filename));
  }

  @Test
  public void givenJobId_whenGetJobStatus_thenReturnStatus200WithJobStatus()
    throws Exception {

    given(service.getJobStatus(500L)).willReturn(BatchStatus.COMPLETED);

    mockMvc.perform(get("/xml/status/{id}", 500L))
      .andExpect(status().is2xxSuccessful())
      .andExpect(status().is(200))
      .andExpect(jsonPath("$").exists())
      .andExpect(jsonPath("$").value(BatchStatus.COMPLETED.toString()));
  }

}
