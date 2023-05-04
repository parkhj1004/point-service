package org.point.provider;

import lombok.RequiredArgsConstructor;
import org.point.domain.repository.Point;
import org.point.domain.repository.UsedPoint;
import org.point.domain.repository.PointLogRepository;
import org.point.domain.repository.PointQueryDslRepository;
import org.point.domain.repository.PointRepository;
import org.point.domain.repository.entity.PointEntity;
import org.point.domain.repository.entity.PointLogEntity;
import org.point.meta.PointActionType;
import org.point.policy.PointPolicy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.point.meta.PointActionType.CANCEL;
import static org.point.meta.PointActionType.SAVE;
import static org.point.meta.PointActionType.USE;
import static org.point.utils.DateUtils.getCurrentLocalDateTime;

@RequiredArgsConstructor
@Component
public class PointProvider {
    private final PointPolicy pointPolicy;
    private final PointRepository pointRepository;
    private final PointQueryDslRepository pointQueryDslRepository;
    private final PointLogRepository pointLogRepository;

    public Long getRemainingPointByMemberId(Long memberId) {
        Map<Long, Long> sumByPointId = getSumByPointId(memberId);
        return sumByPointId.values().stream().mapToLong(Long::longValue).sum();
    }

    public Map<Long, Long> getSumByPointId(Long memberId){
        List<UsedPoint> usedPoints = getValidPointsByMemberId(memberId);

        Map<PointActionType, Map<Long, Long>> pointsMapById = pointPolicy.getPointsMapById(usedPoints);
        return pointPolicy.getSumByPointId(pointsMapById.get(SAVE), pointsMapById.get(USE));
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
        pointRepository.updateHasRemainingPointByIdIn(false, getCurrentLocalDateTime(), usedUpPointIds);

        List<PointLogEntity> pointLogEntities = pointLogRepository.saveAll(entities);

        return pointLogEntities;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int cancelPoint(Long orderId, Set<Long> usedPointIds) {
        int updateCount = pointRepository.updatePointActionTypeByOrderId(CANCEL, getCurrentLocalDateTime(), orderId);
        updateCount += pointRepository.updateHasRemainingPointByIdIn(true, getCurrentLocalDateTime(), usedPointIds);
        updateCount += pointLogRepository.updateActionTypeByOrderIdUsedPointIdsIn(CANCEL, getCurrentLocalDateTime(), orderId, usedPointIds);

        return updateCount;
    }

}
