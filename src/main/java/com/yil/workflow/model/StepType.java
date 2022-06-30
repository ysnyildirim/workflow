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

/*
 * Start: Should only be one per process. This state is the state into which a new Request is placed when it is created.
 * Normal: A regular state with no special designation.
 * Complete: A state signifying that any Request in this state have completed normally.
 * Denied: A state signifying that any Request in this state has been denied (e.g. never got started and will not be worked on).
 * Cancelled: A state signifying that any Request in this state has been cancelled (e.g. work was started but never completed).
 * */
@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(schema ="WFS",name = "STEP_TYPE")
public class StepType implements IEntity {
    @Id
    @SequenceGenerator(name = "STEP_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_STEP_TYPE_ID",schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "STEP_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
}
