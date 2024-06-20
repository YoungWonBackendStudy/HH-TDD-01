package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointConcurrencyTest {
    @Autowired
    PointService pointService;

    @RepeatedTest(3)
    void concurrencyTestChargePoint() throws InterruptedException {
        //given: 사용자
        long testUserId = 0;
        long pointToCharge = 100;
        int executionCnt = 10;
        UserPoint testUser = pointService.getUserPoint(testUserId);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(executionCnt);
        Runnable chargePoint = () -> {
            try{
                pointService.chargeUserPoint(testUserId, pointToCharge);
            } finally {
                latch.countDown();
            }
        };

        //when: 100포인트를 동시에 10번 충전할 때
        
        for (int i = 0; i < executionCnt; i++) {
            executorService.submit(chargePoint);
        }
        latch.await();
        executorService.shutdown();

        //then: 1000포인트 충전
        long pointAfter = pointService.getUserPoint(testUserId).point();
        assertThat(pointAfter).isEqualTo(testUser.point() + pointToCharge * executionCnt);
    }

    @RepeatedTest(3)
    void concurrencyTestUsePoint() throws InterruptedException {
        //given: 포인트가 충분히 있는 사용자
        long testUserId = 0;
        long pointToUse = 100;
        int executionCnt = 10;
        UserPoint testUser = pointService.chargeUserPoint(testUserId, pointToUse * executionCnt);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(executionCnt);
        Runnable usePoint = () -> {
            try{
                pointService.useUserPoint(testUserId, pointToUse);
            } finally {
                latch.countDown();
            }
        };
        

        //when: 100포인트를 동시에 10번 사용할 때
        for (int i = 0; i < executionCnt; i++) {
            executorService.submit(usePoint);
        }
        latch.await();
        executorService.shutdown();

        //then: 1000포인트 감소
        long pointAfter = pointService.getUserPoint(testUserId).point();
        assertThat(pointAfter).isEqualTo(testUser.point() - pointToUse * executionCnt);
    }

    @RepeatedTest(3)
    void concurrencyTestAll() throws InterruptedException {
        //given: 포인트가 충분히 있는 사용자
        long testUserId = 0;
        long testAmount = 100;
        int executionCnt = 10; //반드시 짝수
        UserPoint testUser = pointService.chargeUserPoint(testUserId, testAmount * executionCnt);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(executionCnt);
        Runnable chargePoint = () -> {
            try{
                pointService.chargeUserPoint(testUserId, testAmount);
            } finally {
                latch.countDown();
            }
        };
        Runnable usePoint = () -> {
            try{
                pointService.useUserPoint(testUserId, testAmount);
            } finally {
                latch.countDown();
            }
        };
        
        //when: 충전/사용을 번갈아 가면서 실행할 때
        for (int i = 0; i < executionCnt; i++) {
            if(i % 2 == 0) {
                executorService.submit(chargePoint);
            } else {
                executorService.submit(usePoint);
            }
        }
        latch.await();
        executorService.shutdown();

        //then: 포인트 변화 없음 (실행 횟수는 짝수)
        long pointAfter = pointService.getUserPoint(testUserId).point();
        assertThat(pointAfter).isEqualTo(testUser.point() - testAmount * executionCnt);
    }

}
