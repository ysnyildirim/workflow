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
 * Approve: The actioner is suggesting that the request should move to the next state.
 * Deny: The actioner is suggesting that the request should move to the previous state.
 * Cancel: The actioner is suggesting that the request should move to the Cancelled state in the process.
 * Restart: The actioner suggesting that the request be moved back to the Start state in the process.
 * Resolve: The actioner is suggesting that the request be moved all the way to the Completed state.
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ACTION_TYPE")
public class ActionType implements IEntity {
    @Id
    @SequenceGenerator(name = "ACTION_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_TYPE_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "ACTION_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
}
