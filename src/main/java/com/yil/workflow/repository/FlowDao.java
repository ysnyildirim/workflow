package com.yil.workflow.repository;

import com.yil.workflow.model.Flow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowDao extends JpaRepository<Flow, Long> {

    List<Flow> findAllByDeletedTimeIsNull();

    Optional<Flow> findByIdAndDeletedTimeIsNull(Long id);

    Optional<Flow> findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id);

    boolean existsByIdAndEnabledTrueAndDeletedTimeIsNull(Long id);

    @Query(nativeQuery = true,
            value = "SELECT *\n" +
                    "FROM wfs.flow f\n" +
                    "WHERE f.enabled = 1\n" +
                    "    AND EXISTS (\n" +
                    "        SELECT 1 FROM wfs.step s\n" +
                    "        WHERE s.flow_id = f.id\n" +
                    "            AND s.enabled = 1\n" +
                    "            AND s.step_type_id = 1\n" +
                    "            AND EXISTS (\n" +
                    "                SELECT 1 FROM wfs.action a\n" +
                    "                WHERE a.step_id = s.id\n" +
                    "                    AND a.enabled = 1\n" +
                    "                    AND EXISTS (\n" +
                    "                        SELECT 1 FROM wfs.action_source ats\n" +
                    "                        WHERE ats.action_id = a.id\n" +
                    "                            AND ats.target_type_id = 3\n" +
                    "                            AND EXISTS (\n" +
                    "                                SELECT 1 FROM wfs.flow_group_user fg\n" +
                    "                                WHERE fg.flow_group_id = ats.flow_group_id\n" +
                    "                                    AND fg.user_id =:P_USER_ID\n" +
                    "                            )\n" +
                    "                    )\n" +
                    "            )\n" +
                    "    )")
    List<Flow> getStartUpFlows(@Param(value = "P_USER_ID") long userId);

}
