package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TaskActionMessage")
public class TaskActionMessage extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TaskActionMessage_Sequence_Generator",
            sequenceName = "Seq_TaskActionMessage_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "TaskActionMessage_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "TaskActionId", nullable = false)
    private Long taskActionId;
    @Column(name = "MessageId", nullable = false)
    private Long messageId;
}
