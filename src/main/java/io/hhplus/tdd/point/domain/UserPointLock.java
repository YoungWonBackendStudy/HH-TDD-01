package io.hhplus.tdd.point.domain;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Synchronized;

public class UserPointLock {
    private static final Logger log = LoggerFactory.getLogger(PointService.class);

    private final Set<Long> userLock = new HashSet<>();
    private final int MAX_LOCK_WAIT = 1000;

    public void waitForLock(long userId) throws Exception {
        for(int i = 0; i < this.MAX_LOCK_WAIT; i++) {
            if(this.lock(userId)) return; 

            try {
                Thread.sleep(500);
            } catch(InterruptedException e) {
                log.error("Thread sleep error", e);
            }
        }

        throw new Exception("LOCK Timeout");
    }

    public void releaseLock(long userId) {
        userLock.remove(userId);
    }

    @Synchronized
    private boolean lock(long userId) {
        if(userLock.contains(userId))
            return false;
        
        userLock.add(userId);
        return true;
    }
}
