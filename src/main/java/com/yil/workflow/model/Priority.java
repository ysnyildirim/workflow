package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PRIORITY")
public class Priority extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "PRIORITY_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_PRIORITY_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "PRIORITY_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;
}
