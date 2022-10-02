package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "WFS",
        name = "TASK_ACTION_MESSAGE")
public class TaskActionMessage implements IEntity {
    @Id
    @SequenceGenerator(name = "TASK_ACTION_MESSAGE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ACTION_MESSAGET_ID", schema = "WFS")
    @GeneratedValue(generator = "TASK_ACTION_MESSAGE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "SUBJECT", nullable = false, length = 100)
    private String subject;
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;
    @Column(name = "TASK_ACTION_ID", nullable = false)
    private Long taskActionId;
}
