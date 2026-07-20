package model;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import service.JobExecutor;
import strategy.SchedulingStrategy;
import strategy.SchedulingStrategyFactory;

/**
 * Core engine: one dispatcher thread blocks on {@link DelayQueue}, worker pool runs jobs.
 *
 * <p>Implement {@link #start()} roughly as:
 * <ol>
 *   <li>Loop while running: {@code entry = queue.take()}</li>
 *   <li>Skip if task cancelled or next time changed (stale entry)</li>
 *   <li>Submit to executor: run {@link JobExecutor#run(Task)}, then strategy.computeNext...</li>
 *   <li>If shouldReschedule and next &gt; 0: update task and {@code queue.put(new ScheduledTaskEntry(task))}</li>
 * </ol>
 */
public class Scheduler {
    private final DelayQueue<ScheduledTaskEntry> queue;
    private final ExecutorService workerPool;
    private final JobExecutor jobExecutor;
    private final AtomicBoolean running;
    private Thread dispatcherThread;

    public Scheduler(int workerThreads) {
        this.queue = new DelayQueue<>();
        this.workerPool = Executors.newFixedThreadPool(workerThreads);
        this.jobExecutor = new JobExecutor();
        this.running = new AtomicBoolean(false);
    }

    public void submit(Task task) {
        queue.offer(new ScheduledTaskEntry(task));
    }

    public void start() {
        // TODO: set running true, start dispatcher thread with loop described in class Javadoc
    }

    public void shutdown() {
        // TODO: running false, interrupt dispatcher, shutdown worker pool gracefully
    }

    protected void onRunFinished(Task task, long finishedAtMs) {
        SchedulingStrategy strategy = SchedulingStrategyFactory.forType(task.getTaskType());
        if (!strategy.shouldReschedule(task)) {
            return;
        }
        long next = strategy.computeNextExecutionTimeMs(task, finishedAtMs);
        if (next > 0) {
            task.setNextExecutionTimeMs(next);
            task.setTaskStatus(enums.TaskStatus.SCHEDULED);
            queue.offer(new ScheduledTaskEntry(task));
        }
    }

    protected JobExecutor getJobExecutor() {
        return jobExecutor;
    }

    protected ExecutorService getWorkerPool() {
        return workerPool;
    }

    protected DelayQueue<ScheduledTaskEntry> getQueue() {
        return queue;
    }

    protected AtomicBoolean getRunning() {
        return running;
    }

    protected void setDispatcherThread(Thread dispatcherThread) {
        this.dispatcherThread = dispatcherThread;
    }

    protected Thread getDispatcherThread() {
        return dispatcherThread;
    }
}
