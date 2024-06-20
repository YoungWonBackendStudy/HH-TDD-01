package io.hhplus.tdd.point.domain;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private static final Logger log = LoggerFactory.getLogger(PointService.class);
    PointHistoryRepository pointHistoryRepository;
    UserPointRepository userPointRepository;

    private final UserPointLock userLock = new UserPointLock();
    
    public PointService(PointHistoryRepository pointHistoryRepository, UserPointRepository userPointRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
        this.userPointRepository = userPointRepository;
    }

    public UserPoint getUserPoint(long userId) {
        return userPointRepository.findById(userId);
    }

    public List<PointHistory> getPointHistory(long userId) {
        return pointHistoryRepository.findPointHistoriesByUserId(userId);
    }

    public UserPoint chargeUserPoint(long userId, long chargePoint){
        if(chargePoint <= 0) 
            throw new RuntimeException("0이하의 포인트는 충전할 수 없습니다.");

        try {
            userLock.waitForLock(userId);
        } catch (Exception e) {
            log.error("userLock error", e);
            throw new RuntimeException(e.getMessage());
        }

        UserPoint chargeRes;
        try{
            long currentPoint = userPointRepository.findById(userId).point();
            chargeRes = userPointRepository.save(userId, currentPoint + chargePoint);
    
            pointHistoryRepository.save(userId, chargePoint, TransactionType.CHARGE, System.currentTimeMillis());
        } finally {
            userLock.releaseLock(userId);
        }

        return chargeRes;
    }

    public UserPoint useUserPoint(long userId, long usePoint) {
        if(usePoint <= 0) 
            throw new RuntimeException("0보다 적은 포인트는 사용할 수 없습니다.");
        
        try {
            userLock.waitForLock(userId);
        } catch (Exception e) {
            log.error("userLock error", e);
            throw new RuntimeException(e.getMessage());
        }

        UserPoint useRes;
        try{
            long currentPoint = userPointRepository.findById(userId).point();
            if(currentPoint < usePoint)
                throw new RuntimeException("포인트가 부족합니다.");
            useRes = userPointRepository.save(userId, currentPoint - usePoint);
            pointHistoryRepository.save(userId, usePoint, TransactionType.USE, System.currentTimeMillis());
        } finally{
            userLock.releaseLock(userId);
        }
        
        return useRes;
    }
}
