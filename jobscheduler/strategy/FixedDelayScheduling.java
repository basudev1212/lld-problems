package strategy;

import model.Task;

public class FixedDelayScheduling implements SchedulingStrategy {

    @Override
    public long computeNextExecutionTimeMs(Task task, long finishedAtMs) {
        return finishedAtMs + task.getFixedDelayMs();
    }

    @Override
    public boolean shouldReschedule(Task task) {
        return !task.isCancelled();
    }
}
