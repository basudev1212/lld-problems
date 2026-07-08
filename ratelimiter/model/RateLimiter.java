package model;

import enums.RateLimiterType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RateLimiter {
    public RateLimiterConfig rateLimiterConfig;
    public RateLimiterType rateLimiterType;

    // One lock object per bucket key (e.g. per userId), so unrelated users
    // never wait on each other. Only requests for the SAME key are serialized.
    private final Map<String, Object> lockPerKey = new ConcurrentHashMap<String, Object>();

    public RateLimiter(RateLimiterConfig rateLimiterConfig, RateLimiterType rateLimiterType) {
        this.rateLimiterConfig = rateLimiterConfig;
        this.rateLimiterType = rateLimiterType;
    }

    public abstract boolean allowRequest(String userId);

    // Returns the single lock object associated with this key, creating it
    // atomically on first use. Subclasses should synchronize on this object
    // (instead of the whole allowRequest method) around their bucket logic.
    protected Object getLockForKey(String key) {
        Object newLock = new Object();
        Object existingLock = lockPerKey.putIfAbsent(key, newLock);
        if (existingLock != null) {
            return existingLock;
        }
        return newLock;
    }

}
