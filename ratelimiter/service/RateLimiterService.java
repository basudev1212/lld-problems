package service;

import enums.RateLimiterType;
import enums.UserType;
import factory.RateLimiterFactory;
import java.util.HashMap;
import java.util.Map;
import model.RateLimiter;
import model.RateLimiterConfig;
import model.User;

public class RateLimiterService {

    // One RateLimiter instance per user type, each wired with its own algorithm + config.
    private final Map<UserType, RateLimiter> rateLimiterByUserType = new HashMap<>();

    public RateLimiterService(){
        RateLimiterFactory rateLimiterFactory = new RateLimiterFactory();

        RateLimiterConfig freeUserConfig = new RateLimiterConfig(3, 10);
        RateLimiter freeRateLimiter = rateLimiterFactory.createRateLimiter(freeUserConfig, RateLimiterType.FIXEDWINDOW);
        rateLimiterByUserType.put(UserType.FREE, freeRateLimiter);

        RateLimiterConfig premiumUserConfig = new RateLimiterConfig(10, 10);
        RateLimiter premiumRateLimiter = rateLimiterFactory.createRateLimiter(premiumUserConfig, RateLimiterType.TOKENBUCKET);
        rateLimiterByUserType.put(UserType.PREMIUM, premiumRateLimiter);
    }

    // The service decides WHICH RateLimiter to use based on the user's type.
    // The chosen RateLimiter then decides WHICH bucket to use based on the userId.
    public boolean allowRequest(User user) {
        RateLimiter rateLimiter = rateLimiterByUserType.get(user.userType);
        if (rateLimiter == null) {
            throw new IllegalArgumentException("No rate limiter configured for user type: " + user.userType);
        }
        return rateLimiter.allowRequest(user.userId);
    }
}
