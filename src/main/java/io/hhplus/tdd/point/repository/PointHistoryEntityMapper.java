package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.domain.PointHistory;

public class PointHistoryEntityMapper {
    public static PointHistory toDomain(PointHistoryEntity entity) {
        return new PointHistory(entity.getId(), entity.getUserId(), entity.getAmount(), entity.getType(), entity.getUpdateMillis());
    }
}
