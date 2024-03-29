package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "WFS", name = "TASK_ACTION_DOCUMENT",
        indexes = {
                @Index(name = "IDX_TASK_ACTION_DOCUMENT", columnList = "TASK_ACTION_ID,DOCUMENT_ID", unique = true)
        })
public class TaskActionDocument implements IEntity {
    @Id
    @SequenceGenerator(name = "TASK_ACTION_DOCUMENT_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ACTION_DOCUMENT_ID", schema = "WFS")
    @GeneratedValue(generator = "TASK_ACTION_DOCUMENT_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "DOCUMENT_ID", nullable = false, length = 32)
    private String documentId;
    @Column(name = "TASK_ACTION_ID", nullable = false)
    private Long taskActionId;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "EXTENSION", nullable = false, length = 10)
    private String extension;
}
