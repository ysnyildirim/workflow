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
@Table(name = "STATUS")
public class Status implements IEntity {
    @Id
    @SequenceGenerator(name = "STATUS_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_STATUS_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "STATUS_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;
}
