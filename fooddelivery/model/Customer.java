package model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private final List<Order> orderHistory;

    
    public Customer(String name, Address address) {
        super(name, address);
        orderHistory = new ArrayList<>();
    }


    @Override
    public void onOrderUpdate(Order order) {
        System.err.println("Customer Received Order Status: " + order.getOrderStatus().toString());
    }

    public void addOrderToHistory(Order order){
        orderHistory.add(order);
    }

    public List<Order> getOrderHistory(){
        return orderHistory;
    }
}
