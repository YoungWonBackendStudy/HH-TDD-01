package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.domain.UserPoint;

public class UserPointDtoMapper {
    static UserPointDto toDto(UserPoint userPoint) {
        return UserPointDto.builder()
            .id(userPoint.id())
            .point(userPoint.point())
            .updateMillis(userPoint.updateMillis())
            .build();
    }
}
