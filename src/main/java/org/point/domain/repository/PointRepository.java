package org.point.domain.repository;

import org.point.domain.repository.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, Long> {

    @Modifying
    @Query("update PointEntity p set p.hasRemainingPoints = false where p.id in :usedUpPointIds")
    int updateAllByIdIn(@Param("usedUpPointIds") Set<Long> usedUpPointIds);
}
