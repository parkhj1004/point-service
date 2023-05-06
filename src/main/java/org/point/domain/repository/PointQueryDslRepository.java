package org.point.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.point.meta.PointActionType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.point.domain.repository.entity.QPointEntity.pointEntity;
import static org.point.domain.repository.entity.QPointLogEntity.pointLogEntity;
import static org.point.meta.PointActionType.SAVE;
import static org.point.meta.PointActionType.USE;

@RequiredArgsConstructor
@Repository
public class PointQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Long> findUsedPointIdsByOrderId(Long memberId, Long orderId) {
        return jpaQueryFactory.select(
                Projections.constructor(
                        Long.class,
                        pointLogEntity.usedPointId
                ))
                .from(pointLogEntity)
                .innerJoin(pointEntity)
                .on(pointLogEntity.originPointId.eq(pointEntity.id))
                .where(memberId(memberId))
                .where(orderId(orderId))
                .where(pointActionType(USE))
                .fetch();

    }

    public List<UsedPoint> findValidPointsByMemberId(Long memberId) {

        return jpaQueryFactory.select(
                Projections.constructor(
                        UsedPoint.class,
                        pointEntity.id,
                        pointEntity.point,
                        pointLogEntity.usedPointId,
                        pointLogEntity.point.as("usedPoint")
                ))
                .from(pointEntity)
                .leftJoin(pointLogEntity)
                .on(pointEntity.id.eq(pointLogEntity.usedPointId).and(pointLogEntity.pointActionType.eq(PointActionType.USE)))
                .where(memberId(memberId))
                .where(pointActionType(SAVE))
                .where(pointEntity.expirationDate.goe(now()))
                .where(pointEntity.hasRemainingPoints.eq(true))
                .fetch();
    }

    public List<Point> findPointsByMemberId(Long memberId, Pageable pageable) {
        return jpaQueryFactory.select(
                Projections.constructor(
                        Point.class,
                        pointEntity.createdDate,
                        pointEntity.pointActionType,
                        pointEntity.point,
                        pointEntity.expirationDate
                ))
                .from(pointEntity)
                .where(memberId(memberId))
                .where(pointEntity.pointActionType.in(SAVE, USE))
                .orderBy(pointEntity.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression memberId(Long memberId) {
        return pointEntity.memberId.eq(memberId);
    }

    private BooleanExpression orderId(Long orderId) {
        return pointEntity.orderId.eq(orderId);
    }

    private BooleanExpression pointActionType(PointActionType actionType) {
        return pointEntity.pointActionType.eq(actionType);
    }
}
