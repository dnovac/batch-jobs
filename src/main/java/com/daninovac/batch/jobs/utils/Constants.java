package com.daninovac.batch.jobs.utils;

import java.io.File;


/**
 * @author dan on 27/08/2020
 * @project batch-jobs
 */
public final class Constants {

  //C:\Users\dnovac\AppData\Local\Temp\jobs
  public static final File TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));
  public static final String DELIMITER = "delimiter";
  public static final String PATH = "path";
  public static final String TIME = "time";
  public static final String FILENAME = "filename";
  public static final String DEFAULT_FILENAME = "csv_default";
  public static final String TYPE = "filetype";

  private Constants() {
  }

}
