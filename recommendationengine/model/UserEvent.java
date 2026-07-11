package recommendationengine.model;

import recommendationengine.enums.EventType;

public class UserEvent {
    private User user;
    private Item item;
    private EventType eventType;

    public UserEvent(User user, Item item, EventType eventType) {
        this.user = user;
        this.item = item;
        this.eventType = eventType;
    }

    public Item getItem(){
        return this.item;
    }

    public User getUser(){
        return this.user;
    }

    public EventType getEventType(){
        return this.eventType;
    }
    
}
