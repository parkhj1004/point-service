package org.point.policy;

import org.point.config.properties.PointPolicyHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.time.LocalDateTime;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PointPolicyTest {

    @InjectMocks
    private PointPolicy policy;
    @Mock
    private PointPolicyHolder policyHolder;

    @Test
    public void getExpirationDate() {
        int period = 3;

        when(policyHolder.getPointUsagePeriod()).thenReturn(period);
        LocalDateTime expireDate = policy.getExpirationDate(null);

//        assertThat(expireDate).isEqualTo(addDay(period));
    }
}