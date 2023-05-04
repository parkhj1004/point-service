package org.point.domain.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPointEntity is a Querydsl query type for PointEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointEntity extends EntityPathBase<PointEntity> {

    private static final long serialVersionUID = -1588576182L;

    public static final QPointEntity pointEntity = new QPointEntity("pointEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> expirationDate = createDateTime("expirationDate", java.time.LocalDateTime.class);

    public final BooleanPath hasRemainingPoints = createBoolean("hasRemainingPoints");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final NumberPath<Long> point = createNumber("point", Long.class);

    public final EnumPath<org.point.meta.PointActionType> pointActionType = createEnum("pointActionType", org.point.meta.PointActionType.class);

    public QPointEntity(String variable) {
        super(PointEntity.class, forVariable(variable));
    }

    public QPointEntity(Path<? extends PointEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPointEntity(PathMetadata metadata) {
        super(PointEntity.class, metadata);
    }

}

