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
@Table(schema = "WFS", name = "\"GROUP\"")
public class Group extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "GROUP_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_GROUP_ID", schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "GROUP_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
}
