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
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(schema = "WFS", name = "TASK_ACTION_NEXT")
public class TaskActionNext implements IEntity {
    @EmbeddedId
    private Pk pk;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Id
        @Column(name = "TARGET_ID")
        private Long targetId;
        @Id
        @Column(name = "TASK_ACTION_ID", nullable = false)
        private Long taskActionId;
    }


}

