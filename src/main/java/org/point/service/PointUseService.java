package org.point.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.point.controller.Point;
import org.point.domain.repository.entity.PointEntity;
import org.point.domain.repository.entity.PointLogEntity;
import org.point.dto.ResultDto;
import org.point.policy.PointPolicy;
import org.point.provider.PointProvider;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.point.meta.ResultCode.LACK_OF_POINT;
import static org.point.meta.ResultCode.SUCCESS_USED_POINT;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointUseService implements Integrator {

    private final PointProvider pointProvider;
    private final PointPolicy pointPolicy;
    @Override
    public ResultDto integrate(Long memberId, Point point) {

        Map<Long, Long> sumByPointId = pointProvider.getSumByPointId(memberId);
        boolean hasAvailablePoints = pointPolicy.hasAvailablePoints(sumByPointId, point.getPoint());

        if(hasAvailablePoints) {
            PointEntity pointEntity = PointEntity.of(memberId, false, point);
            pointEntity.setPoint(pointPolicy.pointCalculator(pointEntity.getPoint(), pointEntity.getPointActionType()));

            List<PointLogEntity> pointLogs = pointProvider.usePoint(pointEntity, pointPolicy.getUsePoints(sumByPointId, point));

            return ResultDto.of(SUCCESS_USED_POINT, point.getOrderId(), getUsedPointIds(pointLogs));
        }

        return ResultDto.of(LACK_OF_POINT, point.getOrderId(), null);
    }

    private Set<Long> getUsedPointIds(List<PointLogEntity> pointLogs) {
        if(isEmpty(pointLogs)) {
            return new HashSet<>();
        }

        return pointLogs.stream().map(PointLogEntity::getUsedPointId).collect(Collectors.toSet());
    }
}
