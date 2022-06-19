package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TASK_ACTION_DOCUMENT",
        indexes = {
                @Index(name = "IDX_TASK_ACTION_DOCUMENT_TASK_ACTION_ID_DOCUMENT_ID",
                        columnList = "TASK_ACTION_ID,DOCUMENT_ID")})
public class TaskActionDocument extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TASK_ACTION_DOCUMENT_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ACTION_DOCUMENT_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "TASK_ACTION_DOCUMENT_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "TASK_ACTION_ID", nullable = false)
    private Long taskActionId;
    @Column(name = "DOCUMENT_ID", nullable = false)
    private Long documentId;
}
