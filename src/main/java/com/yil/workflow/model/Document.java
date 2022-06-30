package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(schema = "WFS", name = "DOCUMENT")
public class Document implements IEntity {
    @Id
    @SequenceGenerator(name = "DOCUMENT_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_DOCUMENT_ID", schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "DOCUMENT_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT", nullable = false)
    private Byte[] content;

}
