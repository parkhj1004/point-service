package org.point.provider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.point.config.properties.PointPolicyHolder;
import org.point.domain.repository.PointLogRepository;
import org.point.domain.repository.PointQueryDslRepository;
import org.point.domain.repository.PointRepository;
import org.point.domain.repository.UsedPoint;
import org.point.meta.PointActionType;
import org.point.policy.PointPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.point.meta.PointActionType.SAVE;
import static org.point.meta.PointActionType.USE;

@RunWith(MockitoJUnitRunner.class)
public class PointProviderTest {

    @InjectMocks
    private PointProvider pointProvider;
    @Mock
    private PointPolicy pointPolicy;
    @Mock
    private PointPolicyHolder policyHolder;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private PointQueryDslRepository pointQueryDslRepository;
    @Mock
    private PointLogRepository pointLogRepository;

    @Test
    public void getRemainingPointByMemberId() {
        Long memberId = 333L;
        Map<Long, Long> sumByPointId = new HashMap<>();
        sumByPointId.put(1L, 1000L);
        sumByPointId.put(2L, 2000L);
        when(pointProvider.getSumByPointId(memberId)).thenReturn(sumByPointId);

        Long remainingPoint = pointProvider.getRemainingPointByMemberId(memberId);
        assertThat(remainingPoint).isEqualTo(3000L);
    }

    @Test
    public void getSumByPointId() {
        Long memberId = 333L;
        List<UsedPoint> usedPoints = new ArrayList();
        usedPoints.add(new UsedPoint(1L, 1000L, 1L, -500L));
        usedPoints.add(new UsedPoint(1L, 1000L, 1L, -400L));

        Map<PointActionType, Map<Long, Long>> pointsMapById = new HashMap<>();
        pointsMapById.put(SAVE, new HashMap<>() {{ put(1L, 1000L); }});
        pointsMapById.put(USE, new HashMap<>() {{ put(1L, -900L); }});

        when(pointProvider.getValidPointsByMemberId(memberId)).thenReturn(anyList());
        when(pointPolicy.getPointsMapById(usedPoints)).thenReturn(pointsMapById);
        when(pointPolicy.getSumByPointId(pointsMapById.get(SAVE), pointsMapById.get(USE))).thenReturn(anyMap());

        Map<Long, Long> sumByPointId = pointProvider.getSumByPointId(memberId);
        assertThat(sumByPointId).isNotNull();
    }
}