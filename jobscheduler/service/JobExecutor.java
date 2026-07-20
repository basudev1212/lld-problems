package service;

import enums.TaskStatus;
import model.Task;

/**
 * Runs a single task body and updates status. Does not reschedule; the scheduler
 * loop uses {@link strategy.SchedulingStrategy} after this returns.
 */
public class JobExecutor {

    public void run(Task task) {
        if (task.isCancelled()) {
            return;
        }
        task.setTaskStatus(TaskStatus.RUNNING);
        try {
            task.getJob().execute();
            task.setTaskStatus(TaskStatus.COMPLETED);
        } catch (Exception e) {
            task.setTaskStatus(TaskStatus.FAILED);
            // Optional: rethrow or log; your scheduler decides retry policy
        }
    }
}
