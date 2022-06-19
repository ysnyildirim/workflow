package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ACTION")
public class Action extends AbstractEntity {

    @Id
    @SequenceGenerator(name = "ACTION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_ID",
            initialValue = 1,
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
    @Column(name = "PERMISSION_ID")
    private Long permissionId;
    @Column(name = "NEXT_FLOW_ID", nullable = false)
    private Long nextFlowId;
    @Column(name = "NEXT_STEP_ID", nullable = false)
    private Long nextStepId;
    @Column(name = "NEXT_GROUP_ID")
    private Long nextGroupId;
    @Column(name = "NEXT_USER_ID")
    private Long nextUserId;
    @Column(name = "STATUS_ID")
    private Long statusId;

}
