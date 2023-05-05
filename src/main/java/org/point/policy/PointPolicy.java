package org.point.policy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.point.config.properties.PointPolicyHolder;
import org.point.controller.Point;
import org.point.domain.repository.UsedPoint;
import org.point.domain.repository.entity.PointLogEntity;
import org.point.meta.PointActionType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.toMap;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;
import static org.point.meta.PointActionType.SAVE;
import static org.point.meta.PointActionType.USE;
import static org.point.meta.PointActionType.isPlus;
import static org.point.utils.DateUtils.format;
import static org.point.utils.DateUtils.getLocalDateTimeAddDay;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
@Component
public class PointPolicy {

    private final PointPolicyHolder policyHolder;

    public LocalDateTime getExpirationDate(String expirationDate) {
        return isNull(expirationDate) ? getLocalDateTimeAddDay(policyHolder.getPointUsagePeriod()) : format(expirationDate);
    }

    public Map<PointActionType, Map<Long, Long>> getPointsMapById (List<UsedPoint> pointLogs) {
        Map<PointActionType, Map<Long, Long>> pointsMapById = new HashMap<>();
        if(isEmpty(pointLogs)) {
            return pointsMapById;
        }

        Map<Long, Long> savedPoints = pointLogs.parallelStream().collect(toMap(UsedPoint::getId, UsedPoint::getPoint, (oldValue, newValue) -> newValue));
        Map<Long, Long> usedPoints = pointLogs.parallelStream().filter(usedPoint -> nonNull(usedPoint.getUsedPointId()))
                .collect(groupingBy(UsedPoint::getUsedPointId, summingLong(UsedPoint::getUsedPoint)));

        pointsMapById.put(SAVE, savedPoints);
        pointsMapById.put(USE, usedPoints);

        return pointsMapById;
    }

    public boolean hasAvailablePoints(Map<Long, Long> sumByPointId, Long usePoint) {
        return isNotEmpty(sumByPointId) && usePoint <= sumByPointId.values().stream().filter(Objects::nonNull).mapToLong(Long::longValue).sum();
    }

    public Map<Long, Long> getSumByPointId(Map<Long, Long> savedPoints, Map<Long, Long> usedPoint) {
        if(isEmpty(savedPoints)) {
            return new TreeMap<>();
        }

        Map<Long, Long> combinedMap = new TreeMap<>(savedPoints);
        if(isEmpty(usedPoint)) {
            return combinedMap;
        }

        usedPoint.forEach((key, value) -> combinedMap.merge(key, value, Long::sum));

        return combinedMap;
    }

    public Long pointCalculator(Long point, PointActionType pointActionType) {
        if(isNull(point) || isNull(pointActionType)) {
            return 0L;
        }

        return isPlus(pointActionType) ? point : point * -1;
    }

    public List<PointLogEntity> getUsePoints(Map<Long, Long> sumByPointId, Point point) {
        List<PointLogEntity> pointLogEntities = new ArrayList<>();
        if(isEmpty(sumByPointId) || isNull(point) || isNull(point.getPoint())) {
            return pointLogEntities;
        }

        Long usePoint = point.getPoint();
        for(Long pointId : sumByPointId.keySet()) {
            if(sumByPointId.get(pointId) < usePoint) {
                usePoint -= sumByPointId.get(pointId);
                pointLogEntities.add(PointLogEntity.of(pointId, point.getOrderId(), USE, pointCalculator(sumByPointId.get(pointId), USE), pointId));
            } else {
                pointLogEntities.add(PointLogEntity.of(pointId, point.getOrderId(), USE, pointCalculator(usePoint, USE)));
                return pointLogEntities;
            }
        }

        return pointLogEntities;
    }

    public boolean nonValidForCancel(Point point) {
        return !isValidForCancel(point);
    }

    public boolean isValidForCancel(Point point) {
        return nonNull(point) && nonNull(point.getOrderId()) && nonNull(point.getPointActionType()) && isNotEmpty(point.getUsedPointIds());
    }
}
