package com.daninovac.batch.jobs.batch.reader.csv;

import com.daninovac.batch.jobs.entity.FileData;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;


@Slf4j
@NoArgsConstructor
public class CsvFieldSetMapper implements FieldSetMapper<FileData> {

  /**
   * @param fieldSet
   * @return FileData
   */
  @SuppressWarnings("NullableProblems")
  @Override
  public FileData mapFieldSet(FieldSet fieldSet) {

    List<String> columnNames = Arrays.asList(fieldSet.getNames());
    ConcurrentMap<String, String> columnProperties = columnNames.parallelStream()
        .collect(Collectors.toConcurrentMap(name -> name,
            fieldSet::readString,
            (prop1, prop2) -> prop1
        ));

    FileData fileData = new FileData();
    fileData.setProperties(columnProperties);

    return fileData;
  }
}
