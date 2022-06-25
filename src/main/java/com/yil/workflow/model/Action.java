package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ACTION")
public class Action extends AbstractEntity {

    @Id
    @SequenceGenerator(name = "ACTION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "ACTION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;
    @Column(name = "STEP_ID", nullable = false)
    private Long stepId;
    @Column(name = "NEXT_STEP_ID", nullable = false)
    private Long nextStepId;
    @Column(name = "ACTION_TYPE_ID", nullable = false)
    private Integer actionTypeId;
}
