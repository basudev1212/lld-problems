import enums.UserType;
import model.User;
import service.RateLimiterService;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RateLimiterApplication {

    public static void main(String[] args) throws InterruptedException {
        RateLimiterService rateLimiterService = new RateLimiterService();

        User user1 = new User("U1", UserType.FREE);
        User user2 = new User("U2", UserType.PREMIUM);
        User user3 = new User("U3", UserType.FREE);

        User[] users = new User[] { user1, user2, user3 };
        int requestsPerUser = 6;

        // corePoolSize=6, maximumPoolSize=6 -> always exactly 6 worker threads alive.
        // keepAliveTime=0 -> irrelevant here since core and max pool size are equal.
        // workQueue -> holds submitted tasks until a worker thread is free to run them.
        int poolSize = 6;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                poolSize,
                poolSize,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()
        );

        System.out.println("Sending " + requestsPerUser + " requests each for " + users.length + " users...\n");

        for (int i = 0; i < users.length; i++) {
            for (int requestNumber = 1; requestNumber <= requestsPerUser; requestNumber++) {
                RequestTask task = new RequestTask(rateLimiterService, users[i], requestNumber);
                threadPoolExecutor.execute(task);
            }
        }

        // No more tasks will be submitted -> stop accepting new work and let
        // the already-queued/running tasks finish.
        threadPoolExecutor.shutdown();

        // Block main until either every submitted task has finished, or the timeout elapses.
        boolean finishedInTime = threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES);
        if (!finishedInTime) {
            System.out.println("Timed out waiting for requests to finish.");
        }

        System.out.println("\nAll requests processed.");
    }

    private static class RequestTask implements Runnable {
        private RateLimiterService rateLimiterService;
        private User user;
        private int requestNumber;

        RequestTask(RateLimiterService rateLimiterService, User user, int requestNumber) {
            this.rateLimiterService = rateLimiterService;
            this.user = user;
            this.requestNumber = requestNumber;
        }

        @Override
        public void run() {
            try {
                // Space requests out slightly so windows/buckets have time to behave realistically.
                Thread.sleep(requestNumber * 200);

                boolean allowed = rateLimiterService.allowRequest(user);
                String status = allowed ? "ALLOWED" : "REJECTED";

                System.out.println("User=" + user.userId + " (" + user.userType + ") "
                        + "Request#" + requestNumber + " -> " + status);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
