/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import com.yil.workflow.base.IEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode
@Data
@Entity
@Table(name = "FLOW_GROUP_USER")
public class FlowGroupUser implements IEntity {

    @EmbeddedId
    private Pk id;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "FLOW_GROUP_ID")
        private Long flowGroupId;
        @Column(name = "USER_ID")
        private Long userId;
    }
}
