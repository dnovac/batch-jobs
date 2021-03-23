package com.daninovac.batch.jobs.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.daninovac.batch.jobs.exception.InvalidFileExtensionException;
import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
import org.junit.jupiter.api.Test;


class FileUtilsTest {

  @Test
  void givenFilename_checkFileExtension_validateExtensionIsValidCSV()
    throws InvalidFileExtensionException {

    String fileName = "someFileWithOKExtension.csv";
    final FileTypeEnum fileExtension = FileUtils.getFileExtension(fileName);

    assertTrue(FileTypeEnum.contains(fileExtension.name()));
    assertEquals(FileTypeEnum.CSV, fileExtension);
    assertEquals("csv", fileExtension.extension);

  }

  @Test
  void givenFilename_checkFileExtension_validateExtensionIsValidXML()
    throws InvalidFileExtensionException {

    String fileNameXML = "someFileWithOKExtension.xml";
    final FileTypeEnum fileExtension = FileUtils.getFileExtension(fileNameXML);

    assertTrue(FileTypeEnum.contains(fileExtension.name()));
    assertEquals(FileTypeEnum.XML, fileExtension);
    assertEquals("xml", fileExtension.extension);
  }

  @Test
  void givenFilename_checkFileExtension_throwInvalidFileExtension() {

    String fileNameInvalid = "someFileWithInvalidExtension.doc";
    Exception exception = assertThrows(InvalidFileExtensionException.class, () -> {
      FileUtils.getFileExtension(fileNameInvalid);
    });

    String expectedMessage = "not supported";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

}
