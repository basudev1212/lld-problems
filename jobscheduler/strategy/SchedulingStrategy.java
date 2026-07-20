package strategy;

import model.Task;

public interface SchedulingStrategy {
    /**
     * @param task task that just finished (or failed)
     * @param finishedAtMs wall-clock time when the run ended
     * @return next execution time in epoch millis, or {@code -1} if no reschedule
     */
    long computeNextExecutionTimeMs(Task task, long finishedAtMs);

    /** False for one-shot tasks after they have run once. */
    boolean shouldReschedule(Task task);
}
