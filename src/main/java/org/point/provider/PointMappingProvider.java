package org.point.provider;

import lombok.RequiredArgsConstructor;
import org.point.meta.PointActionType;
import org.point.service.Integrator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Component
public class PointMappingProvider {

    private final Set<Integrator> integrators;
    private Map<Class<? extends Integrator>, Integrator> integratorsMap;

    @PostConstruct
    public void init() {
        if(isEmpty(integrators)) {
            return;
        }

        integratorsMap = integrators.stream().collect(toMap(Integrator::getClass, Function.identity()));
    }

    public Integrator getIntegrator(PointActionType pointActionType) {
        Class<? extends Integrator> integratorType = pointActionType.getIntegratorType();

        return Optional.ofNullable(integratorsMap.get(integratorType))
                .orElseThrow(() -> new IllegalArgumentException("Unknown integrator type. : " + integratorType));
    }
}
