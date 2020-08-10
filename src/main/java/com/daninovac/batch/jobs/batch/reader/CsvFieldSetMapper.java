package com.daninovac.batch.jobs.batch.reader;

import com.daninovac.batch.jobs.entity.FileData;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;


@Slf4j
@NoArgsConstructor
public class CsvFieldSetMapper implements FieldSetMapper<FileData> {

  @Override
  public FileData mapFieldSet(FieldSet fieldSet) throws BindException {

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
