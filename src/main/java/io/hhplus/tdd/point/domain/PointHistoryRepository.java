package io.hhplus.tdd.point.domain;

import java.util.List;

public interface PointHistoryRepository {
    public List<PointHistory> findPointHistoriesByUserId(long UserId);
    public PointHistory save(long userId, long amount, TransactionType type, long updateMillis);
}
