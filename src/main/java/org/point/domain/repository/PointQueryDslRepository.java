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
import static org.point.meta.PointActionType.USED;

@RequiredArgsConstructor
@Repository
public class PointQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Long findRemainingPointByMemberId(Long memberId) {
        return jpaQueryFactory.select(pointEntity.point.add(pointLogEntity.point).sum())
                .from(pointEntity)
                .innerJoin(pointLogEntity)
                .on(pointEntity.id.eq(pointLogEntity.usedPointId))
                .where(pointEntity.memberId.eq(memberId))
                .where(pointActionType(SAVE))
                .fetchOne();
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
                .on(pointEntity.id.eq(pointLogEntity.usedPointId).and(pointLogEntity.pointActionType.eq(PointActionType.USED)))
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
                .where(pointEntity.pointActionType.in(SAVE, USED))
                .orderBy(pointEntity.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression memberId(Long memberId) {
        return pointEntity.memberId.eq(memberId);
    }

    private BooleanExpression pointActionType(PointActionType actionType) {
        return pointEntity.pointActionType.eq(actionType);
    }
}
