package model;

import java.util.concurrent.atomic.AtomicBoolean;

public class DeliveryPartner extends User {
    private final AtomicBoolean isAvailable;

    public DeliveryPartner(String name, Address currentLocation){
        super(name, currentLocation);
        isAvailable = new AtomicBoolean(true);
    }

    @Override
    public void onOrderUpdate(Order order){
        System.err.println("Delivery Partner " + name + " received order status: " + order.getOrderStatus());
    }

    public boolean isAvailable(){
        return this.isAvailable.get();
    }

    public void setAvailable(boolean currentAvailability) {
        this.isAvailable.set(currentAvailability);
    }

    public void updateLocation(Address address){
        this.address = address;
    }
}
