package strategy;

import enums.RateLimiterType;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.RateLimiter;
import model.RateLimiterConfig;

public class SlidingWindowRateLimiter extends RateLimiter {

    private final Map<String, LinkedList<Long>> requestTimestampsPerUser = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(RateLimiterConfig rateLimiterConfig) {
        super(rateLimiterConfig, RateLimiterType.SLIDINGWINDOW);
    }

    @Override
    public boolean allowRequest(String userId) {
        // Lock only this user's timestamp list, so other users are never blocked waiting.
        synchronized (getLockForKey(userId)) {
            int maxRequests = rateLimiterConfig.getMaxRequests();
            int windowSizeInSeconds = rateLimiterConfig.getWindowSizeInSeconds();
            long currentTimeInSeconds = System.currentTimeMillis() / 1000;

            LinkedList<Long> timestamps = requestTimestampsPerUser.get(userId);
            if (timestamps == null) {
                timestamps = new LinkedList<>();
                requestTimestampsPerUser.put(userId, timestamps);
            }

            // Drop timestamps that have fallen outside the sliding window
            Iterator<Long> iterator = timestamps.iterator();
            while (iterator.hasNext()) {
                long timestamp = iterator.next();
                if (currentTimeInSeconds - timestamp >= windowSizeInSeconds) {
                    iterator.remove();
                }
            }

            if (timestamps.size() < maxRequests) {
                timestamps.add(currentTimeInSeconds);
                return true;
            }

            return false;
        }
    }

}
