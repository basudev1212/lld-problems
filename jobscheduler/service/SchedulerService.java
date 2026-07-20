package service;

import enums.TaskType;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import model.Job;
import model.Scheduler;
import model.Task;

/**
 * Facade for clients: build {@link Task} objects, register them, hand off to {@link Scheduler}.
 */
public class SchedulerService {
    private static SchedulerService instance;

    private final Scheduler scheduler;
    private final Map<String, Task> tasksById;

    public SchedulerService(int workerThreads) {
        this.scheduler = new Scheduler(workerThreads);
        this.tasksById = new ConcurrentHashMap<>();
    }

    public static SchedulerService getInstance() {
        if (instance == null) {
            instance = new SchedulerService(4);
        }
        return instance;
    }

    public void start() {
        scheduler.start();
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public String scheduleOneTime(Job job, String name, long delayMs) {
        long runAt = System.currentTimeMillis() + delayMs;
        return registerAndSubmit(job, name, TaskType.ONETIME, runAt, 0, 0);
    }

    public String scheduleFixedDelay(Job job, String name, long initialDelayMs, long delayMs) {
        long runAt = System.currentTimeMillis() + initialDelayMs;
        return registerAndSubmit(job, name, TaskType.FIXEDDELAY, runAt, delayMs, 0);
    }

    public String scheduleFixedRate(Job job, String name, long initialDelayMs, long periodMs) {
        long runAt = System.currentTimeMillis() + initialDelayMs;
        return registerAndSubmit(job, name, TaskType.FIXEDRATE, runAt, 0, periodMs);
    }

    public boolean cancel(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            return false;
        }
        task.cancel();
        return true;
    }

    public Task getTask(String taskId) {
        return tasksById.get(taskId);
    }

    private String registerAndSubmit(
            Job job,
            String name,
            TaskType type,
            long nextRunMs,
            long fixedDelayMs,
            long fixedRateMs) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, name, type, job, nextRunMs, fixedDelayMs, fixedRateMs);
        tasksById.put(id, task);
        scheduler.submit(task);
        return id;
    }
}
