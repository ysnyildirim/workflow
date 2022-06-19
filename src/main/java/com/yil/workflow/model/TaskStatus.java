package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "TASK_STATUS")
public class TaskStatus extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TASK_STATUS_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_STATUS_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "TASK_STATUS_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}
