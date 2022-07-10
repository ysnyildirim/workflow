package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "WFS", name = "TASK_ACTION",
        indexes = {
                @Index(name = "IDX_TASK_ACTION_TASK_ID", columnList = "TASK_ID"),
                @Index(name = "IDX_TASK_ACTION_PARENT_ID", columnList = "PARENT_ID", unique = true),
                @Index(name = "IDX_TASK_ACTION_CREATED_USER_ID", columnList = "CREATED_USER_ID")
        })
public class TaskAction implements IEntity {
    @Id
    @SequenceGenerator(name = "TASK_ACTION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ACTION_ID", schema = "WFS",
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;
    @Column(name = "CREATED_USER_ID", nullable = false)
    private Long createdUserId;

    @Column(name = "NEXT_GROUP_ID")
    private Long nextGroupId;
    @Column(name = "NEXT_USER_ID")
    private Long nextUserId;

}
