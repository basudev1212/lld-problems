package strategy;

import model.Task;

public class OneTimeScheduling implements SchedulingStrategy {

    @Override
    public long computeNextExecutionTimeMs(Task task, long finishedAtMs) {
        return -1;
    }

    @Override
    public boolean shouldReschedule(Task task) {
        return false;
    }
}
