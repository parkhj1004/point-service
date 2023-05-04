package org.point.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.point.controller.Point;
import org.point.domain.repository.UsedPoint;
import org.point.domain.repository.entity.PointEntity;
import org.point.domain.repository.entity.PointLogEntity;
import org.point.dto.ResultDto;
import org.point.meta.PointActionType;
import org.point.policy.PointPolicy;
import org.point.provider.PointProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.point.meta.PointActionType.SAVE;
import static org.point.meta.PointActionType.USED;
import static org.point.meta.ResultCode.LACK_OF_POINT;
import static org.point.meta.ResultCode.SUCCESS_USED_POINT;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointUseService implements Integrator {

    private final PointProvider pointProvider;
    private final PointPolicy pointPolicy;
    @Override
    public ResultDto integrate(Long memberId, Point point) {
        List<UsedPoint> usedPoints = pointProvider.getValidPointsByMemberId(memberId);

        Map<PointActionType, Map<Long, Long>> pointsMapById = pointPolicy.getPointsMapById(usedPoints);
        Map<Long, Long> sumByPointId = pointPolicy.getSumByPointId(pointsMapById.get(SAVE), pointsMapById.get(USED));
        boolean hasAvailablePoints = pointPolicy.hasAvailablePoints(sumByPointId, point.getPoint());

        if(hasAvailablePoints) {
            PointEntity pointEntity = PointEntity.of(memberId, false, point);
            pointEntity.setPoint(pointPolicy.pointCalculator(pointEntity.getPoint(), pointEntity.getPointActionType()));

            List<PointLogEntity> pointLogs = pointProvider.usePoint(pointEntity, pointPolicy.getUsePoints(sumByPointId, point));

            return ResultDto.of(SUCCESS_USED_POINT, point.getOrderId(), pointLogs.stream().map(PointLogEntity::getUsedPointId).collect(Collectors.toList()));
        }

        return ResultDto.of(LACK_OF_POINT, point.getOrderId(), null);
    }
}
