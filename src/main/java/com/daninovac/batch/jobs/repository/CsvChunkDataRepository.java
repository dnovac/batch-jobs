package com.daninovac.batch.jobs.repository;

import com.daninovac.batch.jobs.entity.CsvDataChunk;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsvChunkDataRepository extends MongoRepository<CsvDataChunk, String> {

  List<CsvDataChunk> findByFilename(String filename);

  List<CsvDataChunk> findByType(String type);
}
