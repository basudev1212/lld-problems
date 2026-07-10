package model;

import dispatcher.Dispatcher;
import enums.Direction;

//Each Floor has a External Button
public class Floor {
    int floorNumber;
    ExternalButton externalButton;

    public Floor(int floorNumber, Dispatcher dispatcher) {
        this.floorNumber = floorNumber;
        this.externalButton = new ExternalButton(dispatcher);
    }

    public Elevator pressButton(Direction direction){
        return externalButton.handleHallCall(floorNumber, direction);
    }
    
}
