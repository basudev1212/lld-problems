package model;

/**
 * Unit of work the scheduler runs. Keep it separate from {@link Task} metadata
 * (type, status, timing).
 */
public interface Job {
    void execute() throws Exception;
}
