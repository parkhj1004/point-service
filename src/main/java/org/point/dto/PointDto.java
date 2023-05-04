package org.point.dto;

import lombok.Getter;
import lombok.Setter;
import org.point.domain.repository.Point;

import java.time.LocalDateTime;

@Setter
@Getter
public class PointDto {
    private LocalDateTime dateOfOccurrence;
    private String type;
    private Long point;
    private LocalDateTime expirationDate;

    public static PointDto transfer(Point point) {
        PointDto dto = new PointDto();
        dto.setDateOfOccurrence(point.getCreatedDate());
        dto.setType(point.getPointActionType().getExplain());
        dto.setPoint(point.getPoint());
        dto.setExpirationDate(point.getExpirationDate());

        return dto;
    }
}
