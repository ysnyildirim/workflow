package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "TASK_ACTION",
        indexes = {
                @Index(name = "IDX_TASK_ACTION_TASK_ID_ACTION_ID", columnList = "TASK_ID,ACTION_ID"),
                @Index(name = "IDX_TASK_ACTION_PARENT_ID", columnList = "PARENT_ID", unique = true),
                @Index(name = "IDX_TASK_ACTION_NEXT_USER_ID", columnList = "NEXT_USER_ID")
        })
public class TaskAction extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TASK_ACTION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ACTION_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "TASK_ACTION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "TASK_ID", nullable = false)
    private Long taskId;
    @Column(name = "ACTION_ID", nullable = false)
    private Long actionId;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "NEXT_USER_ID")
    private Long nextUserId;
}
