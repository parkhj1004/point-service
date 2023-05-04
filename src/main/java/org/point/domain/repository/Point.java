package org.point.domain.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.point.meta.PointActionType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Point {
    private LocalDateTime createdDate;
    private PointActionType pointActionType;
    private Long point;
    private LocalDateTime expirationDate;
}
