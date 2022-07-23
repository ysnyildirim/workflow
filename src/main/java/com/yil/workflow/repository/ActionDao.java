package com.yil.workflow.repository;

import com.yil.workflow.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ActionDao extends JpaRepository<Action, Long> {
    List<Action> findAllByStepIdAndDeletedTimeIsNull(Long stepId);

    Optional<Action> findByIdAndStepId(Long id, Long stepId);

    Optional<Action> findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id);

    Optional<Action> findByIdAndDeletedTimeIsNull(Long id);

    List<Action> findAllByStepIdAndEnabledTrueAndDeletedTimeIsNull(long stepId);

    boolean existsByIdAndNextStepId(long id, long nextStepId);

    long countByIdAndEnabledTrueAndDeletedTimeIsNull(long id);

    @Query(nativeQuery = true,
            value = """
                    SELECT *
                    FROM WFS.ACTION A
                    WHERE A.DELETED_TIME IS NULL
                    	AND A.ENABLED = 1
                    	AND EXISTS
                    		(SELECT 1
                    			FROM WFS.STEP S
                    			WHERE S.ENABLED = 1
                    				AND A.STEP_ID = S.ID
                    				AND S.DELETED_TIME IS NULL
                    				AND EXISTS
                    					(SELECT 1
                    						FROM WFS.FLOW F
                    						WHERE F.ENABLED = 1
                    							AND F.DELETED_TIME IS NULL
                    							AND F.ID = S.FLOW_ID)
                    				AND EXISTS
                    					(SELECT 1
                    						FROM WFS.ACTION A2
                    						WHERE A2.ID =:id
                    							AND A2.NEXT_STEP_ID = S.ID))
                    """)
    List<Action> getNextActions(long id);


}
