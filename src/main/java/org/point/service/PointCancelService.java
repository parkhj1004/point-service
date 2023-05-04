package org.point.service;

import lombok.RequiredArgsConstructor;
import org.point.controller.Point;
import org.point.dto.ResultDto;
import org.point.policy.PointPolicy;
import org.springframework.stereotype.Service;

import static org.point.meta.ResultCode.CANCEL_NON_VALID;

@RequiredArgsConstructor
@Service
public class PointCancelService implements Integrator {

    private final PointPolicy pointPolicy;

    @Override
    public ResultDto integrate(Long memberId, Point point) {
        if(pointPolicy.nonValidForCancel(point)) {
            return ResultDto.of(CANCEL_NON_VALID, point.getOrderId(), null);
        }

        return null;
    }
}
