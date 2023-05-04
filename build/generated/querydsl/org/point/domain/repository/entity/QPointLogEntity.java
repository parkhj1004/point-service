package org.point.domain.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPointLogEntity is a Querydsl query type for PointLogEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointLogEntity extends EntityPathBase<PointLogEntity> {

    private static final long serialVersionUID = -1036720512L;

    public static final QPointLogEntity pointLogEntity = new QPointLogEntity("pointLogEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final NumberPath<Long> point = createNumber("point", Long.class);

    public final EnumPath<org.point.meta.PointActionType> pointActionType = createEnum("pointActionType", org.point.meta.PointActionType.class);

    public final NumberPath<Long> usedPointId = createNumber("usedPointId", Long.class);

    public QPointLogEntity(String variable) {
        super(PointLogEntity.class, forVariable(variable));
    }

    public QPointLogEntity(Path<? extends PointLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPointLogEntity(PathMetadata metadata) {
        super(PointLogEntity.class, metadata);
    }

}

