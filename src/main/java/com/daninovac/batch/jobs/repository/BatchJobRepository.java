package com.daninovac.batch.jobs.repository;

import com.daninovac.batch.jobs.entity.ImportData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BatchJobRepository extends JpaRepository<ImportData, Long> {

  void deleteByFilename(String filename);

  List<ImportData> findByFilename(String filename);

}
