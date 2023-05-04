package org.point.domain.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.point.controller.Point;
import org.point.meta.PointActionType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "point")
public class PointEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long orderId;
    private Long point;
    private LocalDateTime expirationDate;
    private boolean hasRemainingPoints;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointActionType pointActionType;

    public static PointEntity of(Long memberId, boolean hasRemainingPoints, Point point) {
        PointEntity entity = new PointEntity();
        entity.setMemberId(memberId);
        entity.setOrderId(point.getOrderId());
        entity.setPoint(point.getPoint());
        entity.setPointActionType(point.getPointActionType());
        entity.setHasRemainingPoints(hasRemainingPoints);

        return entity;
    }


}