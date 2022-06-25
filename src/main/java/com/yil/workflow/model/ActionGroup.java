package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ACTION_GROUP")
public class ActionGroup implements IEntity {

    @EmbeddedId
    private Pk id;

    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "GROUP_ID", nullable = false)
        private Long groupId;
        @Column(name = "ACTION_ID", nullable = false)
        private Long actionId;
    }

}
