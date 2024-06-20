package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointHistoryIntegTest {
    @Autowired
    PointService pointService;

    @Test
    void testPointHistory() {
        //given
        long testId = 0;

        //when: 본인이 200포인트 충전, 100포인트 사용. 다른 사람이 포인트 충전/사용했을 때
        List<PointHistory> beforeHis = pointService.getPointHistory(testId);
        pointService.chargeUserPoint(testId, 300);
        pointService.useUserPoint(testId, 100);

        pointService.chargeUserPoint(testId + 1, 1000);

        //then: 본인의 포인트 충전/사용 내역 2건 조회
        List<PointHistory> afterHis = pointService.getPointHistory(testId);
        assertThat(afterHis.size()).isEqualTo(beforeHis.size() + 2);

        assertThat(afterHis.get(0).userId()).isEqualTo(testId);
        assertThat(afterHis.get(0).amount()).isEqualTo(300);
        assertThat(afterHis.get(0).type()).isEqualTo(TransactionType.CHARGE);

        assertThat(afterHis.get(1).userId()).isEqualTo(testId);
        assertThat(afterHis.get(1).amount()).isEqualTo(100);
        assertThat(afterHis.get(1).type()).isEqualTo(TransactionType.USE);
    }
}
