package org.point.service;

import org.point.controller.Point;
import org.point.dto.ResultDto;

public interface Integrator {
    // todo :  return 변경 필요 함. : orderId, List<pointId>
    default ResultDto integrate(Long memberId, Point point) { return new ResultDto(); }
}
