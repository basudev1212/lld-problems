package strategy;

import model.Task;

public class FixedRateScheduling implements SchedulingStrategy {

    @Override
    public long computeNextExecutionTimeMs(Task task, long finishedAtMs) {
        long nextStart = task.getPreviousScheduledStartMs() + task.getFixedRateMs();
        task.setPreviousScheduledStartMs(nextStart);
        return nextStart;
    }

    @Override
    public boolean shouldReschedule(Task task) {
        return !task.isCancelled();
    }
}
