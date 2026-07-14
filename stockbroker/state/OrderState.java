package stockbroker.state;

import stockbroker.model.Order;

public interface OrderState {
    void handle(Order order);
    void cancel(Order order);
}
