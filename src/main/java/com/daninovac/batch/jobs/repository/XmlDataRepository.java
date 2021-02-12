package com.daninovac.batch.jobs.repository;

import com.daninovac.batch.jobs.entity.XmlDataDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dan Novac on 24/11/2020
 * @project batch-jobs
 */
@Repository
public interface XmlDataRepository extends MongoRepository<XmlDataDocument, String> {

  List<XmlDataDocument> findByFilename(String filename);


}
