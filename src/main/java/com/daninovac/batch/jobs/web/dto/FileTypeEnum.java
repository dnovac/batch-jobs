package com.daninovac.batch.jobs.web.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum FileTypeEnum {
  CSV("csv"),
  XML("xml");

  private static final Map<String, FileTypeEnum> BY_EXTENSION = new HashMap<>();

  public final String extension;

  static {
    for (FileTypeEnum e : values()) {
      BY_EXTENSION.put(e.extension, e);
    }
  }

  public static boolean contains(String test) {

    for (FileTypeEnum type : FileTypeEnum.values()) {
      if (type.name().equalsIgnoreCase(test)) {
        return true;
      }
    }
    return false;
  }

  public static FileTypeEnum valueOfExtension(String extension) {

    return BY_EXTENSION.get(extension);
  }

  public static String[] getNames(Class<? extends Enum<?>> e) {

    return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
  }
}
