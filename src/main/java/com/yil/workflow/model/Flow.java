package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "FLOW")
public class Flow extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "FLOW_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_FLOW_ID",
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
    @Column(name = "START_UP_PERMISSION_ID")
    private Long startPermissionId;

}
