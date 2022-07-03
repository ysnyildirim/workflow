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

    boolean existsByIdAndEnabledTrueAndDeletedTimeIsNull(Long id);

    @Query(nativeQuery = true,
            value = """
                        select * from WFS.FLOW f
                        where f.ENABLED=1
                        and f.DELETED_TIME IS NULL
                        and exists(
                            select  1 from WFS.STEP s
                            where s.FLOW_ID=f.ID
                            and s.ENABLED=1
                            and s.DELETED_TIME IS NULL
                            and s.STEP_TYPE_ID=1
                            and exists(
                                select 1 from WFS.ACTION a
                                where a.ENABLED=1
                                and a.DELETED_TIME IS NULL
                                and
                                (
                                    (
                                        a.TARGET_TYPE_ID=4
                                        and a.USER_ID=:userId
                                    )
                                    or
                                    (
                                        a.TARGET_TYPE_ID=3
                                        and exists
                                        (
                                            select  1 from WFS.GROUP_USER gu
                                            where gu.GROUP_ID=a.GROUP_ID
                                            and gu.GROUP_USER_TYPE_ID=3
                                            and gu.USER_ID=:userId
                                        )
                                    )
                                )
                            )
                        )
                    """)
    List<Flow> getStartUpFlows(@Param(value = "userId") long userId);

}
