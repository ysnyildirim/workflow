package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TASK_ACTION_MESSAGE",
        indexes = {
                @Index(name = "IDX_TASK_ACTION_MESSAGE_TASK_ACTION_ID_MESSAGE_ID",
                        columnList = "TASK_ACTION_ID,MESSAGE_ID")})
public class TaskActionMessage extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TASK_ACTION_MESSAGE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ACTION_MESSAGE_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "TASK_ACTION_MESSAGE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "TASK_ACTION_ID", nullable = false)
    private Long taskActionId;
    @Column(name = "MESSAGE_ID", nullable = false)
    private Long messageId;
}
