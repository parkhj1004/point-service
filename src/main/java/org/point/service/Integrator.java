package org.point.service;

import org.point.controller.Point;
import org.point.dto.ResultDto;

import static java.util.Objects.isNull;

public interface Integrator {
    default ResultDto integrate(Long memberId, Point point) { return new ResultDto(); }
    default boolean checkPoint(Long point) {
        return isNull(point) || point < 0;
    }
}
