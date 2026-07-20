package model;

import enums.OrderStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import observer.OrderObserver;

public class Order {
    private String id;
    private Customer customer;
    private Restaurant restaurant;
    private List<OrderItem> orderItems;
    private DeliveryPartner deliveryPartner;
    private OrderStatus orderStatus;
    private final List<OrderObserver> observers = new ArrayList<>();

    public Order(Customer customer, Restaurant restaurant, List<OrderItem> orderItems){
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.restaurant = restaurant;
        this.orderItems = orderItems;
        this.orderStatus = OrderStatus.PENDING;
        addObserver(restaurant);
        addObserver(customer);
    }

    public String getId(){
        return this.id;
    }

    public Customer getCustomer(){
        return this.customer;
    }

    public Restaurant getRestaurant(){
        return this.restaurant;
    }

    public List<OrderItem> getOrderItems(){
        return this.orderItems;
    }

    public OrderStatus getOrderStatus(){
        return this.orderStatus;
    }

    public void setDeliveryPartner(DeliveryPartner deliveryPartner){
        this.deliveryPartner = deliveryPartner;
    }

    public DeliveryPartner getDeliveryPartner(){
        return this.deliveryPartner;
    }

    public double getTotalAmount(){
        double total = 0;
        for(OrderItem item : orderItems){
            total += item.getMenuItem().getPrice() * item.getQuantity();
        }
        return total;
    }

    public void setOrderStatus(OrderStatus orderStatus){
        if(this.orderStatus != orderStatus){
            this.orderStatus = orderStatus;
            notifyObservers();
        }
    }

    public void addObserver(OrderObserver orderObserver){
        observers.add(orderObserver);
    }

    private void notifyObservers(){
        for(OrderObserver orderObserver : observers){
            orderObserver.onOrderUpdate(this);
        }
    }



    
}
