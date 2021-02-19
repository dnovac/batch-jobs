package com.daninovac.batch.jobs.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.daninovac.batch.jobs.service.CsvJobService;
import com.daninovac.batch.jobs.web.dto.CsvFileDataDTO;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebMvcTest(CsvJobController.class)
public class CsvJobControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  WebApplicationContext webApplicationContext;

  @MockBean
  private CsvJobService service;

  @Test
  public void givenValidForm_whenFileUploaded_thenReturnStatus202WithJobId()
    throws Exception {

    MockMultipartFile mockMultipartFile = new MockMultipartFile(
      "file",
      "file.csv",
      MediaType.MULTIPART_FORM_DATA_VALUE,
      "Testing;For;A;Better;World".getBytes()
    );

    given(service.runJobCsvImport(";", mockMultipartFile)).willReturn(22L);

    mockMvc.perform(multipart("/csv/import")
      .file(mockMultipartFile)
      .param("delimiter", ";"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(status().is(202))
      .andExpect(jsonPath("$.jobId").exists())
      .andExpect(jsonPath("$.jobId").isNumber())
      .andExpect(jsonPath("$.jobId").value(22));
  }

  @Test
  public void whenFindDataByFilename_thenReturnStatus200WithContent()
    throws Exception {

    final String filename = "test_filename.test";
    Map<String, String> dataMap = new HashMap<String, String>() {{
      put("name", "testingStuff");
      put("title", "justTests");
      put("another_data", "anotherTime");
    }};

    CsvFileDataDTO csvFileDataDTO = CsvFileDataDTO.builder()
      .filename(filename)
      .type(FileTypeEnum.CSV.name())
      .data(Arrays.asList(dataMap)).build();

    given(service.findAllDataByFilename(filename)).willReturn(csvFileDataDTO);

    mockMvc.perform(get("/csv/data/" + filename))
      .andExpect(status().is2xxSuccessful())
      .andExpect(status().is(200))
      .andExpect(jsonPath("$.filename").exists())
      .andExpect(jsonPath("$.type").exists())
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.filename").value(filename));
  }

  @Test
  public void whenGetJobStatus_thenReturnStatus200WithJobStatus()
    throws Exception {

    given(service.getJobStatus(0L)).willReturn(BatchStatus.STARTING);

    mockMvc.perform(get("/csv/status/{id}", 0))
      .andExpect(status().is2xxSuccessful())
      .andExpect(status().is(200))
      .andExpect(jsonPath("$").exists())
      .andExpect(jsonPath("$").value(BatchStatus.STARTING.toString()));
  }

}
