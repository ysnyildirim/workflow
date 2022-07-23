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

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFS", name = "PROPERTIES")
public class Properties implements IEntity {
    @Id
    @SequenceGenerator(name = "PROPERTIES_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_PROPERTIES_ID", schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "PROPERTIES_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Integer id;
    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;
    @Column(name = "VALUE", nullable = false, length = 4000)
    private String value;
}
