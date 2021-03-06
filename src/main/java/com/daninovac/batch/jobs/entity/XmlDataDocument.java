package com.daninovac.batch.jobs.entity;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class XmlDataDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;

    private String filename;

    private String type;

    private org.bson.Document properties;

    private Date createdAt;

}
