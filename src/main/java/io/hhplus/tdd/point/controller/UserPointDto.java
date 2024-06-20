package io.hhplus.tdd.point.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserPointDto{
    long id;
    long point;
    long updateMillis;
}
