package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "DOCUMENT")
public class Document extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "DOCUMENT_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_DOCUMENT_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "DOCUMENT_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT", nullable = false)
    private Byte[] content;

}
