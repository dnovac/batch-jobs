package com.daninovac.batch.jobs.utils;

import java.io.File;


/**
 * @author dan on 27/08/2020
 * @project batch-jobs
 */
public final class Constants {

  public static final File TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));
  public static final String DIRECTORY_NAME = "jobs";
  public static final String DELIMITER = "delimiter";
  public static final String PATH_TO_UPLOADED_FILE = "path";
  public static final String PATH_TO_CONVERTED_XML = "path_to_converted_xml";
  public static final String TIME = "time";
  public static final String FILENAME = "filename";
  public static final String DEFAULT_FILENAME = "csv_default";
  public static final String FILE_EXTENSION = "file_extension";
  public static final String XML_CONVERTED_FILENAME="student.csv"; //todo rename better
  public static final String PATH_TO_TEMP_FOLDER = "path_to_temp_folder";

  private Constants() {
  }

}
