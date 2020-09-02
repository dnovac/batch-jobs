package com.daninovac.batch.jobs.web.dto;

import com.daninovac.batch.jobs.utils.Constants;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


/**
 * @author dan on 27/08/2020
 * @project batch-jobs
 */
public enum FileTypeEnum {
  CSV,
  XLS,
  XLSX;


  public String getName() {

    return this.name().toLowerCase();
  }

}
