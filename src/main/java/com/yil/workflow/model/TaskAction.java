package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TaskAction")
public class TaskAction extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TaskAction_Sequence_Generator",
            sequenceName = "Seq_TaskAction",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "TaskAction_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "TaskId", nullable = false)
    private Long taskId;
    @Column(name = "ActionId", nullable = false)
    private Long actionId;
    @Column(name = "UserId", nullable = false)
    private Long userId;
}
