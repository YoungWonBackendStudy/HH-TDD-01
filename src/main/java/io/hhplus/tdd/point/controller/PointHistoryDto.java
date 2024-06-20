package io.hhplus.tdd.point.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PointHistoryDto{
    long id;
    long userId;
    long amount;
    String type;
    long updateMillis;
}
