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
import static org.springframework.util.CollectionUtils.isEmpty;

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

    public List<Long> findUsedPointIdsByOrderId(Long memberId, Long orderId) {
        return pointQueryDslRepository.findUsedPointIdsByOrderId(memberId, orderId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PointEntity savePoint(PointEntity entity) {
        return pointRepository.save(entity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<PointLogEntity> usePoint(PointEntity pointEntity, List<PointLogEntity> logEntities) {
        PointEntity savedPoint = pointRepository.save(pointEntity);

        Set<Long> usedUpPointIds = logEntities.stream().map(PointLogEntity::getUsedUpPointId).collect(Collectors.toSet());
        pointRepository.updateHasRemainingPointByIdIn(false, getCurrentLocalDateTime(), usedUpPointIds, pointEntity.getMemberId());

        setOriginPointId(logEntities, savedPoint.getId());
        return pointLogRepository.saveAll(logEntities);
    }

    private void setOriginPointId(List<PointLogEntity> logEntities, Long originPointId) {
        if(isEmpty(logEntities)) {
            return;
        }

        logEntities.forEach(entity -> entity.setOriginPointId(originPointId));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void cancelPoint(Long memberId, Long orderId, Set<Long> usedPointIds) {
        pointRepository.updatePointActionTypeByOrderId(CANCEL, getCurrentLocalDateTime(), orderId, memberId);
        pointRepository.updateHasRemainingPointByIdIn(true, getCurrentLocalDateTime(), usedPointIds, memberId);
        pointLogRepository.updateActionTypeByCancel(CANCEL, getCurrentLocalDateTime(), orderId, memberId, usedPointIds);
    }

}
