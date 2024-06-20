package io.hhplus.tdd.point.domain;

public interface UserPointRepository {
    public UserPoint findById(long userId);
    public UserPoint save(long userId, long point);
}
