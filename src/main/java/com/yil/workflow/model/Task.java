package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "TASK")
public class Task extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TASK_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "TASK_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "FLOW_ID", nullable = false)
    private Long flowId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE", nullable = false)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISH_DATE")
    private Date finishDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ESTIMATED_FINISH_DATE")
    private Date estimatedFinishDate;
    @Column(name = "CURRENT_TASK_ACTION_ID", nullable = false)
    private Long currentTaskActionId;
    @Column(name = "TASK_STATUS_ID", nullable = false)
    private Long taskStatusId;
}
