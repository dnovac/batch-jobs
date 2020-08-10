package com.daninovac.batch.jobs.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class)
public class FileData {

  //todo add another table with the filenames and make it PK
  //todo maybe add format as field and have tables based on format

  @Id
  @GeneratedValue
  private Long id;

  private String filename;

  @Type(type = "hstore")
  @Column(columnDefinition = "hstore")
  private Map<String, String> properties = new HashMap<>();

}