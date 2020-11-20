package com.daninovac.batch.jobs.utils;

import static com.daninovac.batch.jobs.utils.Constants.DEFAULT_FILENAME;
import static com.daninovac.batch.jobs.web.dto.FileTypeEnum.getNames;

import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
public class FileUtils {

  private FileUtils() {

  }

  public static File saveFileInTemporaryFolder(File tempUploadedFileDirectory,
      MultipartFile multipartFile) throws IOException {

    createDirectory(tempUploadedFileDirectory);

    if (tempUploadedFileDirectory.exists()) {
      final String originalFilename = getFilename(multipartFile);
      File fileToImport = new File(tempUploadedFileDirectory, originalFilename);

      try (OutputStream outputStream = new FileOutputStream(fileToImport)) {
        IOUtils.copy(multipartFile.getInputStream(), outputStream);
        log.info("Saving file of size {} in temporary folder", multipartFile.getSize());
        outputStream.flush();
      }

      return fileToImport;
    }
    throw new FileNotFoundException();
  }

  /**
   * Create directory if not exists
   *
   * @param tempUploadedFileDirectory temporary created directory which hosts the uploaded file
   * @implNote on Windows worked without this func, but MacOS needed it
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static void createDirectory(File tempUploadedFileDirectory) {

    if (!tempUploadedFileDirectory.exists()) {
      tempUploadedFileDirectory.mkdir();
    }
  }

  /**
   * @param multipartFile uploaded file
   * @return filename with extension attached
   */
  public static String getFilename(MultipartFile multipartFile) {

    return Optional.ofNullable(multipartFile.getOriginalFilename())
        .orElse(DEFAULT_FILENAME);
  }

  /**
   * Extracts the extension from the file name
   *
   * @param filename - name of the uploaded file
   * @return one of the types of extensions from FileTypeEnum
   * @throws InvalidFileExtensionException exception
   */
  public static FileTypeEnum getFileExtension(String filename)
      throws InvalidFileExtensionException {

    @SuppressWarnings("UnstableApiUsage")
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
