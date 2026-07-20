package strategy;

import enums.TaskType;
import java.util.EnumMap;
import java.util.Map;

public final class SchedulingStrategyFactory {
    private static final Map<TaskType, SchedulingStrategy> STRATEGIES =
            new EnumMap<>(TaskType.class);

    static {
        STRATEGIES.put(TaskType.ONETIME, new OneTimeScheduling());
        STRATEGIES.put(TaskType.FIXEDDELAY, new FixedDelayScheduling());
        STRATEGIES.put(TaskType.FIXEDRATE, new FixedRateScheduling());
    }

    private SchedulingStrategyFactory() {}

    public static SchedulingStrategy forType(TaskType taskType) {
        SchedulingStrategy strategy = STRATEGIES.get(taskType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy for " + taskType);
        }
        return strategy;
    }
}
