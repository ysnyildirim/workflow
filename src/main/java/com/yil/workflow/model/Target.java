package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Target")
public class Target extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Target_Sequence_Generator",
            sequenceName = "Seq_Target",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "Target_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
}
