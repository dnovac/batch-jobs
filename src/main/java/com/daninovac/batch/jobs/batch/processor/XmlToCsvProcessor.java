package com.daninovac.batch.jobs.batch.processor;

import com.daninovac.batch.jobs.entity.XmlDataDocument;
import com.daninovac.batch.jobs.utils.Constants;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class XmlToCsvProcessor implements ItemProcessor<Object, XmlDataDocument> {

  private String filename;

  private FileTypeEnum fileType;

  @Override
  public XmlDataDocument process(@SuppressWarnings("NullableProblems") Object data)
      throws JobParametersInvalidException {
    return buildFileData(data);
  }

  private XmlDataDocument buildFileData(Object data) throws JobParametersInvalidException {
    if (data instanceof ImmutableListMultimap) {
      log.info("XML data is being processed...");

      try {
        ImmutableMap<String, Object> immutableMap = ((ImmutableListMultimap) data).asMap();
        return XmlDataDocument.builder()
            .filename(filename)
            .type(fileType.name())
            .properties(immutableMap)
            .createdAt(new Date())
            .build();
      } catch (Exception e) {
        String errorMessage = "XML Properties could not be converted into multimap structure!";
        log.error(errorMessage);
        e.printStackTrace();

        throw new JobParametersInvalidException(errorMessage);
      }
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
