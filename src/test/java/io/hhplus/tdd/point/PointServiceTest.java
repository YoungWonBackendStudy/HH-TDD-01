package io.hhplus.tdd.point;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointServiceTest {
    @Autowired
    PointService pointService;

    @Test
    void testPointHistory() {
        //given
        long testId = 0;
        long testAmount = 200;

        //test
        List<PointHistory> beforeHis = pointService.getPointHistory(testId);
        pointService.chargeUserPoint(testId, testAmount);
        pointService.useUserPoint(testId, testAmount);
        List<PointHistory> afterHis = pointService.getPointHistory(testId);
        assertEquals(beforeHis.size() + 2, afterHis.size());
    }

    @Test
    void testChargeUserPoint() {
        //given
        long testId = 0;
        long testAmount = 200;

        //test
        UserPoint curUserPoint = pointService.getUserPoint(testId);
        UserPoint testRes = pointService.chargeUserPoint(testId, testAmount);
        assertEquals(testId, testRes.id());
        assertEquals(curUserPoint.point() + testAmount, testRes.point());
    }

    @Test
    void testUseUserPoint() {
        //given
        long testId = 0;
        long testAmount = 200;

        //test
        UserPoint curUserPoint = pointService.getUserPoint(testId);
        UserPoint testRes = pointService.useUserPoint(testId, testAmount);
        assertEquals(testId, testRes.id());
        assertEquals(curUserPoint.point() - testAmount, testRes.point());
    }
}
