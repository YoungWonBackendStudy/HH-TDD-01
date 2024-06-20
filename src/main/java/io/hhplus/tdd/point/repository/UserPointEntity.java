package io.hhplus.tdd.point.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserPointEntity {
    long id;
    long point;
    long updateMillis;
}
