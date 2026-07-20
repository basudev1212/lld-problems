package observer;

import model.Order;

public interface OrderObserver {
    public void onOrderUpdate(Order order);
}
