package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PointHistoryEntity {
    long id;
    long userId;
    long amount;
    TransactionType type;
    long updateMillis;
}
