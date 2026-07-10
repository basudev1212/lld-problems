package model;

import enums.Direction;
import enums.Status;

public class Elevator {
    int id;
    int currentFloor;
    Direction direction;
    Status status;
    InternalButton internalButton;

    //Constructor
    public Elevator(int id) {
        this.id = id;
    }

    //Getters
    public int getCurrentId(){
        return this.id;
    }

    public int getCurrentFloor(){
        return this.currentFloor;
    }

    public Status getCurrentStatus(){
        return this.status;
    }

    public Direction getCurrentDirection(){
        return this.direction;
    }

    public InternalButton getInternalButton(){
        return this.internalButton;
    }

    //Setter
    public void setInternalButton(InternalButton internalButton){
        this.internalButton = internalButton;
    }

    public void setCurrentStatus(Status status) {
        this.status = status;
    }

    public void setCurrentDirection(Direction direction) {
        this.direction = direction;
    }

    public void moveElevator(int floorNumber){
        System.out.println("    [Elevator-" + id + "] moving " + currentFloor + " -> " + floorNumber);
        this.currentFloor = floorNumber;
    }

}
