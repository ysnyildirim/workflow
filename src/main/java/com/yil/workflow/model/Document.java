package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "DOCUMENT")
public class Document extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "DOCUMENT_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_DOCUMENT_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "DOCUMENT_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "EXTENSION", nullable = false, length = 10)
    private String extension;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT", nullable = false)
    private Byte[] content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPLOADED_DATE", nullable = false)
    private Date uploadedDate;

}
