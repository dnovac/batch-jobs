package com.daninovac.batch.jobs.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;


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

  private String type;

  @Type(type = "hstore")
  @Column(columnDefinition = "hstore")
  private Map<String, String> properties = new HashMap<>();

}
