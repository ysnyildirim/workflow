/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(schema = "WFS", name = "ACTION_PERMISSION")
public class ActionPermission {
    @EmbeddedId
    private Pk id;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "ACTION_ID", nullable = false)
        private Long actionId;
        @Column(name = "ACTION_PERMISSION_TYPE_ID", nullable = false)
        private Integer actionPermissionTypeId;
    }
}
