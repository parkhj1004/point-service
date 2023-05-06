package org.point.domain.repository;

import org.point.domain.repository.entity.PointEntity;
import org.point.meta.PointActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update PointEntity p set p.hasRemainingPoints = :hasRemainingPoints, p.modifiedDate = :modifiedDate where p.id in :usedUpPointIds and p.memberId = :memberId")
    int updateHasRemainingPointByIdIn(@Param("hasRemainingPoints") boolean hasRemainingPoints, @Param("modifiedDate") LocalDateTime modifiedDate,
                                      @Param("usedUpPointIds") Set<Long> usedUpPointIds, @Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("update PointEntity p set p.pointActionType = :pointActionType, p.modifiedDate = :modifiedDate  where p.orderId = :orderId and p.memberId = :memberId")
    int updatePointActionTypeByOrderId(@Param("pointActionType") PointActionType pointActionType, @Param("modifiedDate") LocalDateTime modifiedDate,
                                       @Param("orderId") Long orderId, @Param("memberId") Long memberId);

}
