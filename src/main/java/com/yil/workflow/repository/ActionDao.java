package com.yil.workflow.repository;

import com.yil.workflow.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActionDao extends JpaRepository<Action, Long> {
    List<Action> findAllByStepId(Long stepId);

    Optional<Action> findByIdAndStepId(Long id, Long stepId);

    Optional<Action> findByIdAndEnabledTrue(Long id);

    List<Action> findAllByStepIdAndEnabledTrue(long stepId);

    boolean existsByIdAndNextStepId(long id, long nextStepId);

    @Query(nativeQuery = true,
            value = """
                    SELECT *
                    FROM WFS.ACTION A
                    WHERE A.ENABLED = 1
                    	AND EXISTS
                    		(SELECT 1
                    			FROM WFS.STEP S
                    			WHERE S.ENABLED = 1
                    				AND A.STEP_ID = S.ID
                    				AND EXISTS
                    					(SELECT 1
                    						FROM WFS.FLOW F
                    						WHERE F.ENABLED = 1
                    							AND F.ID = S.FLOW_ID)
                    				AND EXISTS
                    					(SELECT 1
                    						FROM WFS.ACTION A2
                    						WHERE A2.ID =:id
                    							AND A2.NEXT_STEP_ID = S.ID))
                    """)
    List<Action> getNextActions(long id);
}
