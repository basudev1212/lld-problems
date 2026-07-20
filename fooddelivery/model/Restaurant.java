package model;

import java.util.UUID;
import observer.OrderObserver;

public class Restaurant implements OrderObserver {
    private String id;
    private String name;
    private Address address;
    private Menu menu;

    public Restaurant(String name, Address address){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.menu = new Menu();
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public Address getAddress(){
        return this.address;
    }

    public void addMenuItem(MenuItem menuItem){
        this.menu.addItem(menuItem);
    }

    public Menu getMenu(){
        return this.menu;
    }

    @Override
    public void onOrderUpdate(Order order) {
        System.err.println("Restaurant Received Order Status: " + order.getOrderStatus().toString());
    }
}
