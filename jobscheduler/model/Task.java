package model;

import enums.TaskStatus;
import enums.TaskType;

public class Task {
    private final String id;
    private final String name;
    private final TaskType taskType;
    private TaskStatus taskStatus;
    private final Job job;

    /** Epoch millis when this task should run next (or only time for ONETIME). */
    private long nextExecutionTimeMs;

    /** For FIXEDDELAY: wait this long after a run finishes. */
    private long fixedDelayMs;

    /**
     * For FIXEDRATE: period between start times. Next start =
     * previousScheduledStartMs + fixedRateMs (not affected by run duration).
     */
    private long fixedRateMs;
    private long previousScheduledStartMs;

    private volatile boolean cancelled;

    public Task(
            String id,
            String name,
            TaskType taskType,
            Job job,
            long nextExecutionTimeMs,
            long fixedDelayMs,
            long fixedRateMs) {
        this.id = id;
        this.name = name;
        this.taskType = taskType;
        this.taskStatus = TaskStatus.SCHEDULED;
        this.job = job;
        this.nextExecutionTimeMs = nextExecutionTimeMs;
        this.fixedDelayMs = fixedDelayMs;
        this.fixedRateMs = fixedRateMs;
        this.previousScheduledStartMs = nextExecutionTimeMs;
        this.cancelled = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Job getJob() {
        return job;
    }

    public long getNextExecutionTimeMs() {
        return nextExecutionTimeMs;
    }

    public void setNextExecutionTimeMs(long nextExecutionTimeMs) {
        this.nextExecutionTimeMs = nextExecutionTimeMs;
    }

    public long getFixedDelayMs() {
        return fixedDelayMs;
    }

    public long getFixedRateMs() {
        return fixedRateMs;
    }

    public long getPreviousScheduledStartMs() {
        return previousScheduledStartMs;
    }

    public void setPreviousScheduledStartMs(long previousScheduledStartMs) {
        this.previousScheduledStartMs = previousScheduledStartMs;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
        this.taskStatus = TaskStatus.CANCELLED;
    }
}
