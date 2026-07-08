package strategy;

import enums.RateLimiterType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.RateLimiter;
import model.RateLimiterConfig;

public class FixedWindowRateLimiter extends RateLimiter {
    private final Map<String, Integer> requestCountPerUser = new ConcurrentHashMap<>();
    private final Map<String, Long> windowStartPerUser = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimiterConfig rateLimiterConfig) {
        super(rateLimiterConfig, RateLimiterType.FIXEDWINDOW);
    }

    @Override
    public boolean allowRequest(String userId) {
        // Lock only this user's window, so other users are never blocked waiting.
        synchronized (getLockForKey(userId)) {
            long currentTimeInSeconds = System.currentTimeMillis() / 1000;
            int rateLimiterMaxRequests = rateLimiterConfig.getMaxRequests();
            int rateLimiterWindowSizeInSeconds = rateLimiterConfig.getWindowSizeInSeconds();

            Long userWindowStart = windowStartPerUser.get(userId);
            Integer userRequestCount = requestCountPerUser.get(userId);

            boolean windowExpired;

            if(userWindowStart == null){
                windowExpired = true;
            }
            else if(currentTimeInSeconds-userWindowStart >= rateLimiterWindowSizeInSeconds){
                windowExpired = true;
            }
            else{
                windowExpired = false;
            }

            if (windowExpired) {
                // Start a brand new window for this user
                windowStartPerUser.put(userId, currentTimeInSeconds);
                requestCountPerUser.put(userId, 1);
                return true;
            }

            if (userRequestCount < rateLimiterMaxRequests) {
                requestCountPerUser.put(userId, userRequestCount + 1);
                return true;
            }

            return false;
        }
    }
}
