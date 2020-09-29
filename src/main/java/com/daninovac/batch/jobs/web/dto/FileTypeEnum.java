package com.daninovac.batch.jobs.web.dto;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @author dan on 27/08/2020
 * @project batch-jobs
 */
@RequiredArgsConstructor
public enum FileTypeEnum {
  CSV("csv"),
  XML("xml"),
  JSON("json");

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

  public static String[] getNames(Class<? extends Enum<?>> e) {

    return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
  }
}
