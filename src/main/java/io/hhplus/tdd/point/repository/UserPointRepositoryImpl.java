package io.hhplus.tdd.point.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.domain.UserPointRepository;

@Component
public class UserPointRepositoryImpl implements UserPointRepository{
    @Autowired
    UserPointTable userPointTable;
    
    @Override
    public UserPoint findById(long userId) {
        UserPointEntity entity = userPointTable.selectById(userId);
        return UserPointEntityMapper.toDomain(entity);
    }

    @Override
    public UserPoint save(long userId, long point) {
        UserPointEntity entity = userPointTable.insertOrUpdate(userId, point);
        return UserPointEntityMapper.toDomain(entity);
    }
    
}
