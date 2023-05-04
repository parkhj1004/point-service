package org.point.service;

import lombok.RequiredArgsConstructor;
import org.point.controller.Point;
import org.point.dto.ResultDto;
import org.point.policy.PointPolicy;
import org.point.provider.PointProvider;
import org.springframework.stereotype.Service;

import static org.point.meta.ResultCode.CANCEL_NON_UPDATE;
import static org.point.meta.ResultCode.CANCEL_NON_VALID;
import static org.point.meta.ResultCode.SUCCESS_CANCELED_POINT;

@RequiredArgsConstructor
@Service
public class PointCancelService implements Integrator {

    private final PointPolicy pointPolicy;
    private final PointProvider pointProvider;

    @Override
    public ResultDto integrate(Long memberId, Point point) {
        if(pointPolicy.nonValidForCancel(point)) {
            return ResultDto.of(CANCEL_NON_VALID, point.getOrderId(), null);
        }

        int updatedCount = pointProvider.cancelPoint(point.getOrderId(), point.getUsedPointIds());
        if(updatedCount > 0) {
            return ResultDto.of(SUCCESS_CANCELED_POINT, point.getOrderId(), point.getUsedPointIds());
        }

        return ResultDto.of(CANCEL_NON_UPDATE, point.getOrderId(), point.getUsedPointIds());
    }
}
