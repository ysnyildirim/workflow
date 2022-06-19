package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TASK_ACTION",
        indexes = {
        @Index(name = "IDX_TASK_ACTION_TASK_ID_ACTION_ID", columnList = "TASK_ID,ACTION_ID"),
        @Index(name = "IDX_TASK_ACTION_USER_ID", columnList = "USER_ID")})
public class TaskAction extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TASK_ACTION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ACTION_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "TASK_ACTION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "TASK_ID", nullable = false)
    private Long taskId;
    @Column(name = "ACTION_ID", nullable = false)
    private Long actionId;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
}
