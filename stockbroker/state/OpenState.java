package stockbroker.state;

import stockbroker.enums.OrderStatus;
import stockbroker.model.Order;

public class OpenState implements OrderState {

    @Override
    public void handle(Order order) {
        // Open orders wait for matching on the exchange.
    }

    @Override
    public void cancel(Order order) {
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setState(new CancelledState());
    }
}
