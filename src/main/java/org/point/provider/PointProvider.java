package org.point.provider;

import lombok.RequiredArgsConstructor;
import org.point.domain.repository.Point;
import org.point.domain.repository.UsedPoint;
import org.point.domain.repository.PointLogRepository;
import org.point.domain.repository.PointQueryDslRepository;
import org.point.domain.repository.PointRepository;
import org.point.domain.repository.entity.PointEntity;
import org.point.domain.repository.entity.PointLogEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PointProvider {
    private final PointRepository pointRepository;
    private final PointQueryDslRepository pointQueryDslRepository;
    private final PointLogRepository pointLogRepository;

    public Long getRemainingPointByMemberId(Long memberId) {
        return pointQueryDslRepository.findRemainingPointByMemberId(memberId);
    }

    public PointEntity findById(Long memberId) {
        return pointRepository.findById(memberId).orElseGet(PointEntity::new);
    }

    public List<UsedPoint> getValidPointsByMemberId(Long memberId) {
        return pointQueryDslRepository.findValidPointsByMemberId(memberId);
    }

    public List<Point> getPointsByMemberId(Long memberId, Pageable pageable) {
        return pointQueryDslRepository.findPointsByMemberId(memberId, pageable);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PointEntity savePoint(PointEntity entity) {
        return pointRepository.save(entity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<PointLogEntity> usePoint(PointEntity pointEntity, List<PointLogEntity> entities) {
        pointRepository.save(pointEntity);

        Set<Long> usedUpPointIds = entities.stream().map(PointLogEntity::getUsedUpPointId).collect(Collectors.toSet());
        pointRepository.updateAllByIdIn(usedUpPointIds);

        List<PointLogEntity> pointLogEntities = pointLogRepository.saveAll(entities);

        return pointLogEntities;
    }

}
