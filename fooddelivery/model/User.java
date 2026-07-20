package model;

import java.util.UUID;
import observer.OrderObserver;

public abstract class User implements OrderObserver {
    String id;
    String name;
    Address address;

    public User(String name, Address address) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
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

    
}
