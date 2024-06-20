package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

public class PointServiceUnitTest {
    PointService pointService;
    PointHistoryTable mockPointHistory;
    UserPointTable mockUserPointTable;

    //mock Table Injection
    @BeforeEach
    void setup() {
        mockPointHistory = mock(PointHistoryTable.class);
        mockUserPointTable = mock(UserPointTable.class);
        pointService = new PointService(mockPointHistory, mockUserPointTable);
    }

    @Test
    @DisplayName("사용자 포인트 조회")
    void testGetPoint() {
        //given: 200포인트를 소유한 사용자
        UserPoint user = new UserPoint(0, 200, 0);
        when(mockUserPointTable.selectById(user.id()))
            .thenReturn(user);
        
        //when: 포인트를 조회할 때
        UserPoint testRes = pointService.getUserPoint(user.id());

        //then: 자신의 포인트(200)를 정상적으로 조회
        assertThat(testRes.id()).isEqualTo(user.id());
        assertThat(testRes.point()).isEqualTo(user.point());
        
    }

    @Test
    @DisplayName("사용자 포인트 충전")
    void testChargePoint() {
        //given: 200포인트를 소유한 사용자
        UserPoint user = new UserPoint(0, 200, 0);
        when(mockUserPointTable.selectById(user.id()))
            .thenReturn(user);

        when(mockUserPointTable.insertOrUpdate(user.id(), 300))
            .thenReturn(new UserPoint(user.id(), 300, 0)); 
        
        //when: 100 포인트를 충전할 때
        long testAmount = 100;
        UserPoint testRes = pointService.chargeUserPoint(user.id(), testAmount);

        //then: 충전 후 포인트(300)를 정상적으로 조회
        assertThat(testRes.id()).isEqualTo(user.id());
        assertThat(testRes.point()).isEqualTo(300);
        
    }


    @Test
    @DisplayName("사용자 포인트 사용")
    void testUsePoint() {
        //given: 200포인트를 소유한 사용자
        UserPoint user = new UserPoint(0, 200, 0);
        when(mockUserPointTable.selectById(user.id()))
            .thenReturn(user);
        when(mockUserPointTable.insertOrUpdate(user.id(), 100))
            .thenReturn(new UserPoint(user.id(), 100, 0)); 
        
        //when: 100 포인트를 사용할 때
        long testAmount = 100;
        UserPoint testRes = pointService.useUserPoint(user.id(), testAmount);

        //then: 사용후 포인트(100)를 정상적으로 조회
        assertThat(testRes.id()).isEqualTo(user.id());
        assertThat(testRes.point()).isEqualTo(100);   
    }

    @Test
    @DisplayName("포인트 충전/이용 내역 조회 테스트") 
    void testGetPointHistory() {
        //given: 2건의 포인트 충전/사용 이력이 있는 사용자
        UserPoint user = UserPoint.empty(0);
        List<PointHistory> userPointHistory = new ArrayList<>(3);
        userPointHistory.add(new PointHistory(0, user.id(), 100, TransactionType.CHARGE, 0));
        userPointHistory.add(new PointHistory(2, user.id(), 200, TransactionType.USE, 0));

        when(mockPointHistory.selectAllByUserId(0))
            .thenReturn(userPointHistory);
        
        //when: 포인트 충전/사용 이력을 조회할 때
        List<PointHistory> testRes = pointService.getPointHistory(user.id());
        
        //then: 본인의 포인트 충전/사용 이력 2건 조회
        assertThat(testRes.size()).isEqualTo(2);
        assertThat(testRes.get(0)).isEqualTo(userPointHistory.get(0));
        assertThat(testRes.get(1)).isEqualTo(userPointHistory.get(1));
    }

    @Test
    @DisplayName("0이하의 포인트 충전")
    void testChargeLE0() {
        //given: 사용자
        UserPoint user = UserPoint.empty(0);

        //when: 사용자가 -1 포인트를 충전하려 할 때
        //then: ErrorMessage와 함께 Throws RuntimeException
        long testAmount = -1;
        assertThatThrownBy(() -> {
                pointService.chargeUserPoint(user.id(), testAmount);
            }) 
            .isInstanceOf(RuntimeException.class)
            .hasMessage("0이하의 포인트는 충전할 수 없습니다.");
    }

    @Test
    @DisplayName("0보다 적은 포인트 사용")
    void testUseLE0() {
        //given: 사용자 Id
        UserPoint user = UserPoint.empty(0);

        //when: 사용자가 -1 포인트를 사용하려고 할 때
        //then: ErrorMessage와 함께 Throws RuntimeException
        long testAmount = -1;
        assertThatThrownBy(() -> {
                pointService.useUserPoint(user.id(), testAmount);
            })
            .isInstanceOf(RuntimeException.class)
            .hasMessage("0보다 적은 포인트는 사용할 수 없습니다.");
    }

    @Test
    @DisplayName("보유한 포인트보다 많이 사용")
    void testPointLack() {
        //given: 200 포인트를 소유한 사용자
        UserPoint user = new UserPoint(0, 200, 0);
        when(mockUserPointTable.selectById(user.id()))
            .thenReturn(user);

        //when: 사용자가 201 포인트를 사용하려고 할 때
        //then: ErrorMessage와 함께 Throws RuntimeException
        long testAmount = user.point() + 1;
        assertThatThrownBy(() -> {
                pointService.useUserPoint(user.id(), testAmount);
            })
            .isInstanceOf(RuntimeException.class)
            .hasMessage("사용 포인트가 부족합니다.");
    }
}
