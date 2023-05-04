package org.point.service;

import lombok.RequiredArgsConstructor;
import org.point.domain.repository.Point;
import org.point.dto.PointDto;
import org.point.provider.PointProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@RequiredArgsConstructor
@Service
public class PointService {
    private final PointProvider pointProvider;

    public Long getRemainingPointByMemberId(Long memberId) {
        return defaultIfNull(pointProvider.getRemainingPointByMemberId(memberId), 0L);
    }

    public List<PointDto> getPointsById(Long memberId, int pageNumber, int sizePerPage) {

        Pageable pageable = PageRequest.of(pageNumber > 0 ? pageNumber-1 : pageNumber, sizePerPage);
        List<Point> points = pointProvider.getPointsByMemberId(memberId, pageable);

        return points.stream().map(PointDto::transfer).collect(Collectors.toList());
    }
}
