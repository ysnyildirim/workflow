/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "FLOW_GROUP")
public class FlowGroup extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "FLOW_GROUP_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_FLOW_GROUP_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "FLOW_GROUP_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
    @Column(name = "FLOW_ID", nullable = false)
    private Long flowId;
}
