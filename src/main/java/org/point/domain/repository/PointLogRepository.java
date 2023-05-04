package org.point.domain.repository;

import org.point.domain.repository.entity.PointLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointLogRepository extends JpaRepository<PointLogEntity, Long> {
}
