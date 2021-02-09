package com.daninovac.batch.jobs.batch.writer;


import com.daninovac.batch.jobs.entity.CsvDataChunk;
import com.daninovac.batch.jobs.entity.CsvDataDocument;
import com.daninovac.batch.jobs.repository.CsvChunkDataRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class CsvWriter implements ItemWriter<CsvDataDocument> {

  private final CsvChunkDataRepository chunkRepository;

  @Value("#{jobParameters['filename']}")
  private String filename;

  @Value("#{jobParameters['file_extension']}")
  private String type;

  @Override
  public void write(List<? extends CsvDataDocument> data) throws Exception {

    if (data.isEmpty()) {
      log.error("List to write is empty!");
      throw new JobExecutionException("Cannot write empty list!");
    }

    log.info("Writing data chunk of {} from CSV import to database...", data.size());
    List<CsvDataDocument> dataDocuments = new ArrayList<>(data);

    CsvDataChunk chunkOfData = CsvDataChunk.builder()
        .data(dataDocuments)
        .filename(filename)
        .type(type)
        .createdAt(new Date())
        .build();
    chunkRepository.save(chunkOfData);
  }

}
