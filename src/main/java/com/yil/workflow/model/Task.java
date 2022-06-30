package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(schema = "WFS", name = "TASK", indexes = {@Index(name = "IDX_TASK_FLOW_ID", columnList = "FLOW_ID")})
public class Task implements IEntity {
    @Id
    @SequenceGenerator(name = "TASK_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ID", schema = "WFS",
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
    @Column(name = "PRIORITY_TYPE_ID", nullable = false)
    private Integer priorityTypeId;
}
