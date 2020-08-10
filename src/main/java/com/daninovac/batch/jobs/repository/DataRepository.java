package com.daninovac.batch.jobs.repository;

import com.daninovac.batch.jobs.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DataRepository extends JpaRepository<FileData, Long> {

  void deleteByFilename(String filename);

  List<FileData> findByFilename(String filename);

}
