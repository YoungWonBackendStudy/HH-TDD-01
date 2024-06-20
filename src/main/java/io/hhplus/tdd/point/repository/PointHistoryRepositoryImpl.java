package io.hhplus.tdd.point.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.PointHistoryRepository;
import io.hhplus.tdd.point.domain.TransactionType;

@Component
public class PointHistoryRepositoryImpl implements PointHistoryRepository{
    @Autowired
    PointHistoryTable pointHistoryTable;

    @Override
    public List<PointHistory> findPointHistoriesByUserId(long UserId) {
        return pointHistoryTable.selectAllByUserId(UserId).stream()
            .map(PointHistoryEntityMapper::toDomain)
            .toList();
    }

    @Override
    public PointHistory save(long userId, long amount, TransactionType type, long updateMillis) {
        PointHistoryEntity entity = pointHistoryTable.insert(userId, amount, type, updateMillis);
        return PointHistoryEntityMapper.toDomain(entity);
    }
    
}
