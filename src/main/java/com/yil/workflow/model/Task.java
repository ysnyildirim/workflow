package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(schema = "WFS",
        name = "TASK",
        indexes = {
                @Index(name = "IDX_TASK_IS_CLOSED", columnList = "IS_CLOSED")
        })
public class Task implements IEntity {
    @Id
    @SequenceGenerator(name = "TASK_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ID", schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "TASK_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
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
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "IS_CLOSED", nullable = false)
    private boolean isClosed;
}
