package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "STEP")
public class Step extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "STEP_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_STEP_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "STEP_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;
    @Column(name = "FLOW_ID")
    private Long flowId;
}
