package org.point.controller;

import lombok.Getter;
import lombok.Setter;
import org.point.meta.PointActionType;

@Setter
@Getter
public class Point {
    private Long orderId;
    private PointActionType pointActionType;
    private Long point;
}

