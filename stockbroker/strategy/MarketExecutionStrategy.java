package stockbroker.strategy;

import stockbroker.model.Order;

public class MarketExecutionStrategy implements OrderExecutionStrategy {

    @Override
    public boolean canExecute(Order order, Double marketPrice) {
        return marketPrice != null && marketPrice > 0;
    }
}
