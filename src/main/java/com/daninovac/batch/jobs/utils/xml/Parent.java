package com.daninovac.batch.jobs.utils.xml;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Parent {

  private String students;

  @XmlAnyElement(ParentHandler.class)
  public String getStudents() {
    return students;
  }

  public void setStudents(String students) {
    this.students = students;
  }


}
