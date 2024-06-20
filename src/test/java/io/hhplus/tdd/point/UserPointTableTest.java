package io.hhplus.tdd.point;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.tdd.database.UserPointTable;

@SpringBootTest
public class UserPointTableTest {
    @Autowired
    UserPointTable userPointTable;
    
    @Test
    void testUserPointTest() {
        //given
        long testId = 1;

        //when
        userPointTable.selectById(testId);
    }
}
