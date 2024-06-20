package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.domain.PointHistory;

public class PointHistoryDtoMapper {
    public static PointHistoryDto toDto(PointHistory pointHistory) {
        return PointHistoryDto.builder()
            .id(pointHistory.id())
            .userId(pointHistory.userId())
            .amount(pointHistory.amount())
            .type(pointHistory.type().toString())
            .updateMillis(pointHistory.updateMillis())
            .build();
    }
}
