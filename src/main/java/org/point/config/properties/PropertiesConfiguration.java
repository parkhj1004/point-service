package org.point.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value= {PointPolicyHolder.class})
public class PropertiesConfiguration {
}
