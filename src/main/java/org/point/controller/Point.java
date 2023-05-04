package org.point.controller;

import lombok.Getter;
import lombok.Setter;
import org.point.meta.PointActionType;

import java.util.Set;

@Setter
@Getter
public class Point {
    private Long orderId;
    private Set<Long> usedPointIds;
    private PointActionType pointActionType;
    private Long point;

    private String expirationDate;
}

