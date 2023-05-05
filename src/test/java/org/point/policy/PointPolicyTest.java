package org.point.policy;

import org.point.config.properties.PointPolicyHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.point.controller.Point;
import org.point.domain.repository.UsedPoint;
import org.point.domain.repository.entity.PointLogEntity;
import org.point.meta.PointActionType;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.point.meta.PointActionType.SAVE;
import static org.point.meta.PointActionType.USE;
import static org.point.utils.DateUtils.format;
import static org.point.utils.DateUtils.getLocalDateTimeAddDay;

@RunWith(MockitoJUnitRunner.class)
public class PointPolicyTest {

    @InjectMocks
    private PointPolicy policy;
    @Mock
    private PointPolicyHolder policyHolder;

    @Test
    public void getUsePoints_null() {
        assertThat(policy.getUsePoints(null, null)).isEmpty();
    }

    @Test
    public void getUsePoints() {
        Map<Long, Long> sumByPointId = new HashMap<>();
        Point point = new Point();
        point.setPoint(1000L);
        sumByPointId.put(1L, 500L);
        sumByPointId.put(2L, 1000L);

        List<PointLogEntity> pointLogEntities = policy.getUsePoints(sumByPointId,point);
        assertThat(pointLogEntities).isNotEmpty();
        assertThat(pointLogEntities.size()).isEqualTo(2);
        assertThat(pointLogEntities.get(0).getPoint()).isEqualTo(-500L);
        assertThat(pointLogEntities.get(1).getPoint()).isEqualTo(-500L);
    }

    @Test
    public void getSumByPointId_null() {
        assertThat(policy.getSumByPointId(null, null)).isEmpty();
    }

    @Test
    public void getSumByPointId_sort() {
        Map<Long, Long> savedPoints = new HashMap<>();
        Map<Long, Long> usedPoint = new HashMap<>();

        savedPoints.put(2L, 1000L);
        savedPoints.put(1L, 2000L);
        savedPoints.put(3L, 3000L);
        usedPoint.put(1L, -1500L);
        usedPoint.put(2L, -1000L);

        Map<Long, Long> combinedMap = policy.getSumByPointId(savedPoints, usedPoint);
        Set<Long> keys = combinedMap.keySet();

        assertThat(keys.stream().findFirst().orElse(null)).isEqualTo(1L);
        assertThat(combinedMap.get(1L)).isEqualTo(500L);
        assertThat(combinedMap.get(2L)).isEqualTo(0L);
        assertThat(combinedMap.get(3L)).isEqualTo(3000L);
    }

    @Test
    public void getSumByPointId_used_null() {
        Map<Long, Long> savedPoints = new HashMap<>();
        savedPoints.put(1L, 1000L);

        Map<Long, Long> combinedMap = policy.getSumByPointId(savedPoints, null);

        assertThat(combinedMap).isNotEmpty();
        assertThat(combinedMap.get(1L)).isEqualTo(1000L);
    }

    @Test
    public void pointCalculator_null() {
        assertThat(policy.pointCalculator(null, null)).isEqualTo(0L);
    }

    @Test
    public void pointCalculator_plus() {
        assertThat(policy.pointCalculator(1000L, SAVE)).isEqualTo(1000L);
    }

    @Test
    public void pointCalculator_minus() {
        assertThat(policy.pointCalculator(1000L, USE)).isEqualTo(-1000L);
    }

    @Test
    public void hasAvailablePoints_null() {
        boolean hasAvailablePoints = policy.hasAvailablePoints(null, 1000L);

        assertThat(hasAvailablePoints).isFalse();
    }

    @Test
    public void hasAvailablePoints_false() {
        Map<Long, Long> sumByPointId = new HashMap<>();
        sumByPointId.put(1L, 1000L);

        assertThat(policy.hasAvailablePoints(sumByPointId, 2000L)).isFalse();
    }

    @Test
    public void hasAvailablePoints_null_false() {
        Map<Long, Long> sumByPointId = new HashMap<>();
        sumByPointId.put(1L, null);

        assertThat(policy.hasAvailablePoints(sumByPointId, 2000L)).isFalse();
    }

    @Test
    public void getPointsMapById() {
        List<UsedPoint> pointLogs = new ArrayList<>();
        pointLogs.add(new UsedPoint(1L, 1000L, 1L, -500L));
        pointLogs.add(new UsedPoint(1L, 1000L, 1L, -500L));
        pointLogs.add(new UsedPoint(2L, 2000L, 2L, -1500L));

        Map<PointActionType, Map<Long, Long>> pointsMapById = policy.getPointsMapById(pointLogs);

        assertThat(pointsMapById).isNotEmpty();
        assertThat(pointsMapById.get(SAVE)).isNotEmpty();
        assertThat(pointsMapById.get(USE)).isNotEmpty();
        assertThat(pointsMapById.get(SAVE).get(1L)).isEqualTo(1000L);
        assertThat(pointsMapById.get(SAVE).get(2L)).isEqualTo(2000L);
        assertThat(pointsMapById.get(USE).get(1L)).isEqualTo(-1000L);
        assertThat(pointsMapById.get(USE).get(2L)).isEqualTo(-1500L);

    }

    @Test
    public void getPointsMapById_null() {
        Map<PointActionType, Map<Long, Long>> pointsMapById = policy.getPointsMapById(null);

        assertThat(pointsMapById).isEmpty();
    }

    @Test
    public void getExpirationDate_인자가_null() {
        int period = 3;

        when(policyHolder.getPointUsagePeriod()).thenReturn(period);
        LocalDateTime expireDate = policy.getExpirationDate(null);

        assertThat(expireDate).isEqualTo(getLocalDateTimeAddDay(period));
    }

    @Test
    public void getExpirationDate_인자가존재함() {
        String expirationDate = "2023-05-05 17:43:43";

        assertThat(policy.getExpirationDate(expirationDate)).isEqualTo(format(expirationDate));
    }
}