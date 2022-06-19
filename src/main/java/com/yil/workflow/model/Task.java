package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "Task")
public class Task extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Task_Sequence_Generator",
            sequenceName = "Seq_Task_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "Task_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "FlowId", nullable = false)
    private Long flowId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "StartDate", nullable = false)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FinishDate")
    private Date finishDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EstimatedFinishDate")
    private Date estimatedFinishDate;
    @Column(name = "CurrentTaskActionId", nullable = false)
    private Long currentTaskActionId;
    @Column(name = "TaskStatusId", nullable = false)
    private Long taskStatusId;
}
