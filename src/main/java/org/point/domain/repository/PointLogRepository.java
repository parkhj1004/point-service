package org.point.domain.repository;

import org.point.domain.repository.entity.PointLogEntity;
import org.point.meta.PointActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface PointLogRepository extends JpaRepository<PointLogEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update PointLogEntity pl set pl.pointActionType = :pointActionType, pl.modifiedDate = :modifiedDate " +
            "where pl.originPointId = (select p.id from PointEntity p where p.orderId = :orderId and p.memberId = :memberId and p.pointActionType = :pointActionType) " +
            "and pl.usedPointId in :usedPointIds")
    int updateActionTypeByCancel(@Param("pointActionType") PointActionType pointActionType, @Param("modifiedDate") LocalDateTime modifiedDate,
                                 @Param("orderId") Long orderId, @Param("memberId") Long memberId, @Param("usedPointIds") Set<Long> usedPointIds);
}
