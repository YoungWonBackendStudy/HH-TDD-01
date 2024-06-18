package io.hhplus.tdd.point;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

@Service
public class PointService {

    private static final Logger log = LoggerFactory.getLogger(PointService.class);

    @Autowired
    PointHistoryTable pointHistoryTable;

    @Autowired
    UserPointTable userPointTable;

    public UserPoint getUserPoint(long id) {
        return userPointTable.selectById(id);
    }

    public List<PointHistory> getPointHistory(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }

    public UserPoint chargeUserPoint(long id, long amount) {
        pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        long currentPoint = userPointTable.selectById(id).point();
        return userPointTable.insertOrUpdate(id, currentPoint + amount);
    }

    public UserPoint useUserPoint(long id, long amount) {
        pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());
        long currentPoint = userPointTable.selectById(id).point();
        return userPointTable.insertOrUpdate(id, currentPoint - amount);
    }
}
