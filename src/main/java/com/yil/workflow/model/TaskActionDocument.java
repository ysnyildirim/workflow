package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "WFS", name = "TASK_ACTION_DOCUMENT",
        indexes = {
                @Index(name = "IDX_TASK_ACTION_DOCUMENT_TASK_ACTION_ID", columnList = "TASK_ACTION_ID")})
public class TaskActionDocument implements IEntity {
    @Id
    @SequenceGenerator(name = "TASK_ACTION_DOCUMENT_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ACTION_DOCUMENT_ID", schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "TASK_ACTION_DOCUMENT_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "TASK_ACTION_ID", nullable = false)
    private Long taskActionId;
    @Column(name = "DOCUMENT_ID", nullable = false)
    private Long documentId;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "EXTENSION", nullable = false, length = 10)
    private String extension;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPLOADED_DATE", nullable = false)
    private Date uploadedDate;
}
