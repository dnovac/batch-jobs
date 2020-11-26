package com.daninovac.batch.jobs.batch.reader.csv;

import com.daninovac.batch.jobs.entity.CsvDataDocument;
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
public class CsvFieldSetMapper implements FieldSetMapper<CsvDataDocument> {

  /**
   * @param fieldSet
   * @return CsvDataDocument object
   */
  @SuppressWarnings("NullableProblems")
  @Override
  public CsvDataDocument mapFieldSet(FieldSet fieldSet) {

    List<String> columnNames = Arrays.asList(fieldSet.getNames());
    ConcurrentMap<String, String> columnProperties = columnNames.parallelStream()
        .collect(Collectors.toConcurrentMap(name -> name,
            fieldSet::readString,
            (prop1, prop2) -> prop1
        ));

    return CsvDataDocument.builder().properties(columnProperties).build();
  }
}
