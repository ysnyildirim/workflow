/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "WFS", name = "GROUP_USER",
        indexes = {
                @Index(name = "IDX_GROUP_USER_USER_ID", columnList = "USER_ID"),
                @Index(name = "IDX_GROUP_USER_GROUP_USER_TYPE_ID", columnList = "GROUP_USER_TYPE_ID"),
                @Index(name = "IDX_GROUP_USER_GROUP_GROUP_ID", columnList = "GROUP_ID")
        },
        uniqueConstraints = @UniqueConstraint(name = "UNQ_CONS_GROUP_USER_GROUP", columnNames = {"USER_ID", "GROUP_USER_TYPE_ID", "GROUP_ID"}))
public class GroupUser implements IEntity {

    @Id
    @SequenceGenerator(name = "GROUP_USER_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_GROUP_USER_ID", schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "GROUP_USER_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "GROUP_USER_TYPE_ID", nullable = false)
    private Integer groupUserTypeId;
    @Column(name = "GROUP_ID")
    private Long groupId;
}
