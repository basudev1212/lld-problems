package stockbroker.strategy;

import stockbroker.enums.TransactionType;
import stockbroker.model.Order;

public class LimitExecutionStrategy implements OrderExecutionStrategy {

    @Override
    public boolean canExecute(Order order, Double marketPrice) {
        if (marketPrice == null) {
            return false;
        }

        if (order.getTransactionType() == TransactionType.BUY) {
            return marketPrice <= order.getPrice();
        }
        return marketPrice >= order.getPrice();
    }
}
