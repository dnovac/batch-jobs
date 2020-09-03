package com.daninovac.batch.jobs.exception;

import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;

import static com.daninovac.batch.jobs.web.dto.FileTypeEnum.getNames;


/**
 * @author dan on 03/09/2020
 * @project batch-jobs
 */
@Slf4j
public class InvalidFileExtensionException extends Exception {

  public InvalidFileExtensionException(String errorMessage, Throwable error) {
    super(errorMessage, error);
  }

  public InvalidFileExtensionException(String errorMessage) {
    super(errorMessage);
  }

}
