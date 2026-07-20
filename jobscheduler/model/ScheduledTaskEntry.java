package model;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Wraps a {@link Task} for a {@link java.util.concurrent.DelayQueue}. The queue
 * orders by {@link Task#getNextExecutionTimeMs()}.
 */
public class ScheduledTaskEntry implements Delayed {
    private final Task task;

    public ScheduledTaskEntry(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long delayMs = task.getNextExecutionTimeMs() - System.currentTimeMillis();
        return unit.convert(delayMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        long thisTime = task.getNextExecutionTimeMs();
        long otherTime = ((ScheduledTaskEntry) other).task.getNextExecutionTimeMs();
        return Long.compare(thisTime, otherTime);
    }
}
