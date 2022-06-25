/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "STEP")
public class Step extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "STEP_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_STEP_ID",
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
    @Column(name = "FLOW_ID", nullable = false)
    private Long flowId;
    @Column(name = "STATUS_ID", nullable = false)
    private Integer statusId;
    @Column(name = "STEP_TYPE_ID", nullable = false)
    private Integer stepTypeId;
}
