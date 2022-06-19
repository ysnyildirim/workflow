package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "FLOW")
public class Flow extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "FLOW_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_FLOW_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "FLOW_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;
    @Column(name = "STARTUP_STEP_ID", nullable = false)
    private Long startUpStepId;
    @Column(name = "STARTUP_PERMISSION_ID")
    private Long startUpPermissionId;
}
