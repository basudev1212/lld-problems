package model;

import controller.ElevatorController;

public class InternalButton {
    ElevatorController elevatorController;

    public InternalButton(ElevatorController elevatorController) {
        this.elevatorController = elevatorController;
    }

    public void goToFloor(int destinationFloor){
        elevatorController.acceptCarCall(destinationFloor);
    }

}
