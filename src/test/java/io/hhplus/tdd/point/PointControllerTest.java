package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;

@SpringBootTest
@AutoConfigureMockMvc
public class PointControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPoint() throws Exception {
        //given
        long testId = 1;
        ObjectMapper objMapper = new ObjectMapper();

        //when: 포인트 조회할 때
        RequestBuilder getReq = get("/point/" + testId);
        MvcResult getRes = mockMvc.perform(getReq)
            .andExpect(status().isOk())
            .andReturn();
        
        UserPoint testUser = objMapper.readValue(getRes.getResponse().getContentAsString(), UserPoint.class);
        
        //then: 본인의 포인트 조회
        assertThat(testUser.id()).isEqualTo(testId);


        //when: 200포인트를 충전할 때
        long pointBeforeCharge = testUser.point();
        RequestBuilder chargeReq = patch("/point/" + testId + "/charge")
            .contentType("application/json")
            .content("200");

        MvcResult chargeRes = mockMvc.perform(chargeReq)
            .andExpect(status().isOk())
            .andReturn();
        
        testUser = objMapper.readValue(chargeRes.getResponse().getContentAsString(), UserPoint.class);
        
        //then: 본인의 포인트를 200포인트 증가
        assertThat(testUser.id()).isEqualTo(testId);
        assertThat(testUser.point()).isEqualTo(pointBeforeCharge + 200);


        //when: 100포인트 사용할 때
        long pointBeforeUse = testUser.point();
        RequestBuilder useReq = patch("/point/" + testId + "/use")
            .contentType("application/json")
            .content("100");

        MvcResult useRes = mockMvc.perform(useReq)
            .andExpect(status().isOk())
            .andReturn();
        
        testUser = objMapper.readValue(useRes.getResponse().getContentAsString(), UserPoint.class);
        
        //then: 본인의 포인트 100포인트 감소
        assertThat(testUser.id()).isEqualTo(testId);
        assertThat(testUser.point()).isEqualTo(pointBeforeUse - 100);


        //when: 포인트 충전/사용 내역을 조회할 때
        RequestBuilder historyReq = get("/point/" + testId + "/histories");

        MvcResult historyRes = mockMvc.perform(historyReq)
            .andExpect(status().isOk())
            .andReturn();
        
        PointHistory[] histories = objMapper.readValue(historyRes.getResponse().getContentAsString(), PointHistory[].class);
        
        //then: 위 2건 조회
        assertThat(histories.length).isEqualTo(2);
        assertThat(histories[0].userId()).isEqualTo(testId);
        assertThat(histories[1].userId()).isEqualTo(testId);
    }

    @Test
    void testError() throws Exception {
        //given
        long testId = 1;

        //when: 0보다 작은 수를 충전할 때
        RequestBuilder getReq = patch("/point/" + testId + "/charge")
            .contentType("application/json")
            .content("-300");
        //then: 지정된 Error Response 수신
        mockMvc.perform(getReq)
            .andExpect(jsonPath("$.code").value("500"))
            .andExpect(jsonPath("$.message").value("에러가 발생했습니다."))
            .andReturn();
    }
}
