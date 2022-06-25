package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
*  addTaskPriority("Highest", "This problem will block progress");
        addTaskPriority("High", "Serious problem that could block progress");
        addTaskPriority("Medium", "Has the potential to effect progress");
        addTaskPriority("Low", "Minor problem or easily worked around");
        addTaskPriority("Lowest", "Trivial problem with little or no impact on progress");*/
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRIORITY")
public class Priority implements IEntity {
    @Id
    @SequenceGenerator(name = "PRIORITY_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_PRIORITY_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "PRIORITY_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;
}
