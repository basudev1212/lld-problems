package stockbroker.state;

import stockbroker.enums.OrderStatus;
import stockbroker.model.Order;

public class PartiallyFilledState implements OrderState {

    @Override
    public void handle(Order order) {
        // Partially filled orders stay active until fully filled or cancelled.
    }

    @Override
    public void cancel(Order order) {
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setState(new CancelledState());
    }
}
