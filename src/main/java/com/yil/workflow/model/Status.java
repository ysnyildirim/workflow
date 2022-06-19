package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "STATUS")
public class Status extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "STATUS_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_STATUS_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "STATUS_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name="IS_CLOSED",nullable = false)
    private Boolean isClosed;
}
