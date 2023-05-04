package org.point.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.point.dto.PointDto;
import org.point.dto.ResultDto;
import org.point.meta.PointActionType;
import org.point.provider.PointMappingProvider;
import org.point.service.PointService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.point.meta.ResultCode.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;
    private final PointMappingProvider pointMappingProvider;


    @GetMapping(value = "/remaining-point")
    public Long getRemainingPointById(@RequestHeader(value = "memberId") Long memberId) {
        try {
            return pointService.getRemainingPointByMemberId(memberId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0L;
        }
    }

    @GetMapping(value = "/points")
    public List<PointDto> getPointsById(@RequestHeader(value = "memberId") Long memberId, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "20") int sizePerPage) {
        try {
            return pointService.getPointsById(memberId, pageNumber, sizePerPage);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @PostMapping(value = "/{pointActionType}/point")
    public ResultDto savePoints(@RequestHeader(value = "memberId") Long memberId, @PathVariable(value = "pointActionType") PointActionType pointActionType, @RequestBody Point point) {
        point.setPointActionType(pointActionType);
        try {
            return pointMappingProvider.getIntegrator(pointActionType).integrate(memberId, point);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultDto.of(SERVER_ERROR, point.getOrderId(), null);
        }
    }
}
