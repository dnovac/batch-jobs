package com.daninovac.batch.jobs.repository;

import com.daninovac.batch.jobs.entity.CsvDataDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dan Novac on 25/11/2020
 * @project batch-jobs
 */
@Repository
public interface CsvDataRepository extends MongoRepository<CsvDataDocument, String> {

  List<CsvDataDocument> findByFilename(String filename);

  List<CsvDataDocument> findByType(String type);
}
