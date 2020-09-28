package com.daninovac.batch.jobs.batch.model;

import lombok.Data;
import lombok.ToString;


/**
 * @author Dan Novac on 28/09/2020
 * @project batch-jobs
 */

@Data
@ToString
public class Student {

  private Integer rollNo;
  private String name;
  private String department;

}
