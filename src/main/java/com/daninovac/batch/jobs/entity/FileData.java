package com.daninovac.batch.jobs.entity;

import com.daninovac.batch.jobs.web.dto.FileTypeEnum;
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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class)
public class FileData {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String filename;

  private FileTypeEnum type;

  @Type(type = "hstore")
  @Column(columnDefinition = "hstore")
  private Map<String, String> properties = new HashMap<>();

}