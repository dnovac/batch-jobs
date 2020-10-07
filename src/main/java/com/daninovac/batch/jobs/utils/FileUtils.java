package com.daninovac.batch.jobs.utils;

import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

import static com.daninovac.batch.jobs.web.dto.FileTypeEnum.getNames;


@Slf4j
public class FileUtils {

  private FileUtils() {

  }

  public static File saveFileInTemporaryFolder(InputStream fileInputStream, String originalFilename)
          throws IOException {

    File tempUploadedFileDirectory = new File(Constants.TEMP_DIRECTORY, "jobs");

    createDirectory(tempUploadedFileDirectory);

    if (tempUploadedFileDirectory.exists()) {
      final String filename = getFilename(originalFilename);
      File savedFile = new File(tempUploadedFileDirectory, filename);

      try (OutputStream outputStream = new FileOutputStream(savedFile)) {
        IOUtils.copy(fileInputStream, outputStream);
        log.info("Saving imported file: {} in temporary folder", filename);
        outputStream.flush();
      }

      return savedFile;
    }
    throw new FileNotFoundException();
  }

  /**
   * Create directory if not exists
   *
   * @param tempUploadedFileDirectory
   * @implNote on Windows worked without this func, but MacOS needed it
   */
  private static void createDirectory(File tempUploadedFileDirectory) {

    if (!tempUploadedFileDirectory.exists()) {
      tempUploadedFileDirectory.mkdir();
    }
  }

  /**
   * @param filename name of the uploaded file
   * @return filename with extension attached
   */
  public static String getFilename(String filename) {

    return Optional.ofNullable(filename).orElse(Constants.DEFAULT_FILENAME);
  }

  /**
   * Extracts the extension from the file name
   *
   * @param filename - name of the uploaded file
   * @return one of the types of extensions from FileTypeEnum
   * @throws InvalidFileExtensionException exception
   */
  public static FileTypeEnum getFileExtension(String filename) throws InvalidFileExtensionException {

    String fileExtension = Files.getFileExtension(filename);

    if (fileExtension.isEmpty()) {
      log.error("File missing extension!");
      throw new InvalidFileExtensionException("No extension was provided for the file!");
    }

    try {
      return FileTypeEnum.valueOfExtension(fileExtension);
    } catch (IllegalArgumentException exception) {
      log.error("File extension not supported!");
      String errorMessage = String.format(
              "File extension is empty or has invalid extension. Accepted extensions are: %s ",
              String.join(", ", getNames(FileTypeEnum.class)));
      throw new InvalidFileExtensionException(errorMessage, exception);
    }
  }

}
