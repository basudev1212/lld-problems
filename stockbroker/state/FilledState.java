package stockbroker.state;

import stockbroker.model.Order;

public class FilledState implements OrderState {

    @Override
    public void handle(Order order) {
        // Filled orders are terminal.
    }

    @Override
    public void cancel(Order order) {
        throw new IllegalStateException("Cannot cancel a filled order: " + order.getId());
    }
}
