package org.point.service;

import lombok.RequiredArgsConstructor;
import org.point.controller.Point;
import org.point.domain.repository.entity.PointEntity;
import org.point.dto.ResultDto;
import org.point.policy.PointPolicy;
import org.point.provider.PointProvider;
import org.springframework.stereotype.Service;

import java.util.HashSet;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static org.point.meta.ResultCode.FAIL_SAVED_POINT;
import static org.point.meta.ResultCode.SUCCESS_SAVED_POINT;

@RequiredArgsConstructor
@Service
public class PointSaveService implements Integrator {

    private final PointProvider pointProvider;
    private final PointPolicy pointPolicy;

    @Override
    public ResultDto integrate(Long memberId, Point point) {
        PointEntity entity = PointEntity.of(memberId, true, point);
        entity.setExpirationDate(pointPolicy.getExpirationDate());

        PointEntity pointEntity = pointProvider.savePoint(entity);

        if(isNull(pointEntity.getId())) {
            return ResultDto.of(FAIL_SAVED_POINT, point.getOrderId(), null);
        }

        return ResultDto.of(SUCCESS_SAVED_POINT, point.getOrderId(), new HashSet<>(){{add(pointEntity.getId());}});
    }
}
