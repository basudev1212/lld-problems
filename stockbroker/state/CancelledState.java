package stockbroker.state;

import stockbroker.model.Order;

public class CancelledState implements OrderState {

    @Override
    public void handle(Order order) {
        // Cancelled orders are terminal.
    }

    @Override
    public void cancel(Order order) {
        throw new IllegalStateException("Order is already cancelled: " + order.getId());
    }
}
