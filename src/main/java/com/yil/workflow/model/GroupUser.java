/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode
@Data
@Entity
@Table(schema = "WFS", name = "GROUP_USER")
public class GroupUser implements IEntity {

    @EmbeddedId
    private Pk id;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "GROUP_ID")
        private Long groupId;
        @Column(name = "USER_ID")
        private Long userId;
        @Column(name = "GROUP_USER_TYPE_ID")
        private Integer groupUserTypeId;
    }
}
