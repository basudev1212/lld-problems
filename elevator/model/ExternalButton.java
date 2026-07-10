package model;

import dispatcher.Dispatcher;
import enums.Direction;

public class ExternalButton {
    Dispatcher dispatcher;

    public ExternalButton(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public Elevator handleHallCall(int floorNumber, Direction direction){
        return dispatcher.handleHallCall(floorNumber, direction);
    }
    
}
