package org.point.domain.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.point.meta.PointActionType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@Table(name = "pointLog")
public class PointLogEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usedPointId;
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointActionType pointActionType;

    private Long point;

    @Transient
    private Long usedUpPointId;

    public static PointLogEntity of(Long usedPointId, Long orderId, PointActionType pointActionType, Long point) {
        PointLogEntity entity = new PointLogEntity();
        entity.setUsedPointId(usedPointId);
        entity.setOrderId(orderId);
        entity.setPointActionType(pointActionType);
        entity.setPoint(point);

        return entity;
    }

    public static PointLogEntity of(Long usedPointId, Long orderId, PointActionType pointActionType, Long point, Long usedUpPointId) {
        PointLogEntity entity = of(usedPointId, orderId, pointActionType, point);
        entity.setUsedUpPointId(usedUpPointId);

        return entity;
    }

}
