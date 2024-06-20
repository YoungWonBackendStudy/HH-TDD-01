package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.domain.UserPoint;

public class UserPointEntityMapper {
    public static UserPoint toDomain(UserPointEntity entity) {
        return new UserPoint(entity.getId(), entity.getPoint(), entity.getUpdateMillis());
    }
}
