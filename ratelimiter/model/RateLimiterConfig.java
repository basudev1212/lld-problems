package model;

public class RateLimiterConfig {
    private Integer maxRequests;
    private Integer windowSizeInSeconds;

    public RateLimiterConfig(int maxRequests, int windowSizeInSeconds){
        this.maxRequests = maxRequests;
        this.windowSizeInSeconds = windowSizeInSeconds;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public int getWindowSizeInSeconds() {
        return windowSizeInSeconds;
    }
}
