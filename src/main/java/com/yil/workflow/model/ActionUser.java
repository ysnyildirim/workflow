package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ACTION_USER")
public class ActionUser implements IEntity {

    @EmbeddedId
    private Pk id;

    @Data
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "USER_ID", nullable = false)
        private Long userId;
        @Column(name = "ACTION_ID", nullable = false)
        private Long actionId;
    }

}
