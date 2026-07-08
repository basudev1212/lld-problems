package factory;

import enums.RateLimiterType;
import model.RateLimiter;
import model.RateLimiterConfig;
import strategy.FixedWindowRateLimiter;
import strategy.SlidingWindowRateLimiter;
import strategy.TokenBucketRateLimiter;

public class RateLimiterFactory {
    public RateLimiter createRateLimiter(RateLimiterConfig rateLimiterConfig, RateLimiterType rateLimiterType){
        switch (rateLimiterType) {
            case TOKENBUCKET:
                return new TokenBucketRateLimiter(rateLimiterConfig);
            case SLIDINGWINDOW:
                return new SlidingWindowRateLimiter(rateLimiterConfig);
            case FIXEDWINDOW:
                return new FixedWindowRateLimiter(rateLimiterConfig);
            default:
                throw new IllegalArgumentException("Unkwown Rate Limiter Type: " + rateLimiterType);
        }
    }
}
