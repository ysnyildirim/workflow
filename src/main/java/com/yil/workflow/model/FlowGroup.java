/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "WFS", name = "FLOW_GROUP")
public class FlowGroup extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "FLOW_GROUP_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_FLOW_GROUP_ID",schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "FLOW_GROUP_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
    @Column(name = "GROUP_TYPE_ID", nullable = false)
    private Integer groupTypeId;
    @Column(name = "FLOW_ID")
    private Long flowId;

}
