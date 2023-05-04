package org.point.meta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.point.service.Integrator;
import org.point.service.PointCancelService;
import org.point.service.PointSaveService;
import org.point.service.PointUseService;

import java.util.EnumSet;

@RequiredArgsConstructor
public enum PointActionType {
    SAVE("적립", PointSaveService.class),
    USED("사용", PointUseService.class),
    CANCEL("취소", PointCancelService.class)
    ;


    @Getter
    private final String explain;
    @Getter
    private final Class<? extends Integrator> integratorType;
    private final static EnumSet<PointActionType> PLUS = EnumSet.of(SAVE, CANCEL);

    public static boolean isPlus(PointActionType pointActionType) {
        return PLUS.contains(pointActionType);
    }
}
