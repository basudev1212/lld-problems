package stockbroker.strategy;

import stockbroker.model.Order;

public interface OrderExecutionStrategy {
    boolean canExecute(Order order, Double marketPrice);
}
