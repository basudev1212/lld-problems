package strategy;

import enums.RateLimiterType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.RateLimiter;
import model.RateLimiterConfig;

public class TokenBucketRateLimiter extends RateLimiter {

    private final Map<String, Integer> tokenCountPerUser = new ConcurrentHashMap<>();
    private final Map<String, Long> lastRefillTimePerUser = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(RateLimiterConfig rateLimiterConfig) {
        super(rateLimiterConfig, RateLimiterType.TOKENBUCKET);
    }

    @Override
    public boolean allowRequest(String userId) {
        // Lock only this user's bucket, so other users are never blocked waiting.
        synchronized (getLockForKey(userId)) {
            int bucketCapacity = rateLimiterConfig.getMaxRequests();
            int windowSizeInSeconds = rateLimiterConfig.getWindowSizeInSeconds();
            long currentTimeInSeconds = System.currentTimeMillis() / 1000;

            int tokens = refillTokens(userId, bucketCapacity, windowSizeInSeconds, currentTimeInSeconds);

            boolean allowed = false;
            if (tokens > 0) {
                tokens = tokens - 1;
                allowed = true;
                tokenCountPerUser.put(userId, tokens);
            }

            return allowed;
        }
    }

    private int refillTokens(String userId, int bucketCapacity, int windowSizeInSeconds, long currentTimeInSeconds) {
        Integer tokens = tokenCountPerUser.get(userId);
        Long lastRefillTime = lastRefillTimePerUser.get(userId);

        if (tokens == null) {
            // First request from this user, bucket starts full
            tokens = bucketCapacity;
            lastRefillTime = currentTimeInSeconds;
        } else {
            long elapsedSeconds = currentTimeInSeconds - lastRefillTime;
            int refillRatePerSecond = bucketCapacity / windowSizeInSeconds;
            int tokensToAdd = (int) elapsedSeconds * refillRatePerSecond;

            if (tokensToAdd > 0) {
                tokens = Math.min(bucketCapacity, tokens + tokensToAdd);
                lastRefillTime = currentTimeInSeconds;
            }
        }

        tokenCountPerUser.put(userId, tokens);
        lastRefillTimePerUser.put(userId, lastRefillTime);

        return tokens;
    }
}
