package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "DOCUMENT_TYPE")
public class DocumentType extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "DOCUMENT_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_DOCUMENT_TYPE_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "DOCUMENT_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}
