package com.daninovac.batch.jobs.web.dto;

import com.daninovac.batch.jobs.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @author dan on 27/08/2020
 * @project batch-jobs
 */
@RequiredArgsConstructor
public enum FileTypeEnum {
  CSV("csv"),
  XLS("xls"),
  XLSX("xlsx");

  private static final Map<String, FileTypeEnum> BY_EXTENSION = new HashMap<>();

  public final String extension;

  static {
    for (FileTypeEnum e : values()) {
      BY_EXTENSION.put(e.extension, e);
    }
  }

  public static FileTypeEnum valueOfExtension(String extension) {
      return BY_EXTENSION.get(extension);
  }
}
