/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.model;


import com.yil.workflow.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Admin : flow select,edit,update,delete
 * Stake : select edit,update
 */
@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "FLOW_GROUP_TYPE")
public class FlowGroupType implements IEntity {
    @Id
    @SequenceGenerator(name = "FLOW_GROUP_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_FLOW_GROUP_TYPE_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "FLOW_GROUP_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
}
