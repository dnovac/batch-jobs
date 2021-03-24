package com.daninovac.batch.jobs.web;

import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * @author dan on 03/09/2020
 * @project batch-jobs
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidFileExtensionException.class)
  public ResponseEntity<String> handleInvalidFileExtensionException(
      InvalidFileExtensionException e) {

    log.error("Invalid file extension exception occurred: {}", e.getMessage());
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

}
