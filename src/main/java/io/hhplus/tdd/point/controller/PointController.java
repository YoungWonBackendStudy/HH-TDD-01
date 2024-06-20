package io.hhplus.tdd.point.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.tdd.point.domain.PointService;
import io.hhplus.tdd.point.domain.UserPoint;

@RestController
@RequestMapping("/point")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    @Autowired
    PointService pointService;

    @GetMapping("/{id}")
    public UserPointDto point(
            @PathVariable("id") long id
    ) {
        UserPoint userPoint = pointService.getUserPoint(id);
        return UserPointDtoMapper.toDto(userPoint);
    }

    @GetMapping("{id}/histories")
    public List<PointHistoryDto> history(
            @PathVariable("id") long id
    ) {
        return pointService.getPointHistory(id).stream()
            .map(PointHistoryDtoMapper::toDto)
            .toList();
    }

    @PatchMapping("{id}/charge")
    public UserPointDto charge(
            @PathVariable("id") long id,
            @RequestBody long amount
    ) {
        UserPoint chargePointResponse =  pointService.chargeUserPoint(id, amount);
        return UserPointDtoMapper.toDto(chargePointResponse);
    }

    @PatchMapping("{id}/use")
    public UserPointDto use(
            @PathVariable("id") long id,
            @RequestBody long amount
    ) {
        UserPoint usePointResponse = pointService.useUserPoint(id, amount);
        return UserPointDtoMapper.toDto(usePointResponse);
    }
}
