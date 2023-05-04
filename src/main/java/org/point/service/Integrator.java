package org.point.service;

import org.point.controller.Point;
import org.point.dto.ResultDto;

public interface Integrator {
    default ResultDto integrate(Long memberId, Point point) { return new ResultDto(); }
}
