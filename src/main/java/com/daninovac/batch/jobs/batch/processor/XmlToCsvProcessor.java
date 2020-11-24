package com.daninovac.batch.jobs.batch.processor;

import com.daninovac.batch.jobs.entity.FileData;
import com.daninovac.batch.jobs.entity.XmlData;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimaps;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class XmlToCsvProcessor implements ItemProcessor<Object, XmlData> {

  private String filename;

  private FileTypeEnum fileType;

  @Override
  public XmlData process(@SuppressWarnings("NullableProblems") Object data)
      throws JobParametersInvalidException {
    return buildFileData(data);
  }

  private XmlData buildFileData(Object data) throws JobParametersInvalidException {
    if (data instanceof ArrayListMultimap) {
      log.info("XML data is being processed...");
      XmlData xmlData = new XmlData();
      xmlData.setFilename(filename);
      xmlData.setType(fileType.name());

      //todo use immutable multimaps
      ArrayListMultimap<String, Object> dataMultiMap = ArrayListMultimap.create();
      ((ArrayListMultimap<String, Object>) data).forEach(dataMultiMap::put);
      Map<String, List<Object>> mapOfProperties = Multimaps.asMap(dataMultiMap);
      xmlData.setProperties(mapOfProperties);
      return xmlData;
    } else {
      String errorMessage = "Parsed XML data is not valid!";
      log.warn(errorMessage);
      throw new JobParametersInvalidException(errorMessage);
    }
  }

  @BeforeStep
  public void fillParameters(final StepExecution stepExecution) {

    JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
    this.filename = parameters.getString(Constants.FILENAME);
    this.fileType = FileTypeEnum.valueOf(parameters.getString(Constants.FILE_EXTENSION));
  }

}
