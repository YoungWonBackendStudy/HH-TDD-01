package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

@SpringBootTest
public class PointTableTest {
    @Autowired
    UserPointTable userPointTable;

    @Autowired
    PointHistoryTable pointHistoryTable;
    
    @Test
    void testUserPoint() {
        //given
        long testId = 1;

        //when: 300포인트 입력 시
        UserPoint testUser = userPointTable.insertOrUpdate(testId, 300);

        //then: 사용자에게 300포인트 부여
        assertThat(testUser.id()).isEqualTo(testId);
        assertThat(testUser.point()).isEqualTo(300);
        
        //when: 사용자 id로 조회 시
        UserPoint userSelected = userPointTable.selectById(testUser.id());

        //then: 동일한 사용자
        assertThat(userSelected).isEqualTo(testUser);
    }

    @Test
    void testPointHistory() {
        //given
        long testId = 1;

        //when: "300포인트 충전", "200포인트 사용" 내역을 추가했을 때
        PointHistory chargeHistory = pointHistoryTable.insert(testId, 300, TransactionType.CHARGE, 0);
        PointHistory useHistory = pointHistoryTable.insert(testId, 200, TransactionType.USE, 0);

        //then: 사용자 ID로 조회시 위 두 내역 조회
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(testId);
        assertThat(pointHistories.size()).isEqualTo(2);
        assertThat(pointHistories.get(0)).isEqualTo(chargeHistory);
        assertThat(pointHistories.get(1)).isEqualTo(useHistory);
    }
}
