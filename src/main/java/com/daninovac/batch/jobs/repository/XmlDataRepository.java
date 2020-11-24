package com.daninovac.batch.jobs.repository;

import com.daninovac.batch.jobs.entity.FileData;
import com.daninovac.batch.jobs.entity.XmlData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dan Novac on 24/11/2020
 * @project batch-jobs
 */
@Repository

public interface XmlDataRepository extends JpaRepository<XmlData, Long> {

}
