package model;

import controller.ElevatorController;
import dispatcher.Dispatcher;
import java.util.ArrayList;
import java.util.List;

public class Building {
    List<Floor> floors;
    List<Elevator> elevators;
    Dispatcher dispatcher;

    public Building() {
        floors = new ArrayList<>();
        elevators = new ArrayList<>();
        dispatcher = new Dispatcher();
    }

    public void addFloor(int floorNumber){
        Floor floor = new Floor(floorNumber, dispatcher);
        floors.add(floor);
    }

    public void addElevator(int elevatorNumber){
        Elevator elevator = new Elevator(elevatorNumber);
        //wiring elevator with a elevator controller
        ElevatorController elevatorController = new ElevatorController(elevator);
        //wiring internal button with a controller
        InternalButton internalButton = new InternalButton(elevatorController);
        elevator.setInternalButton(internalButton);
        //adding to the list of elevators
        dispatcher.addController(elevatorController);
        elevators.add(elevator);
    }

    public Floor getFloor(int floorNumber){
        return floors.get(floorNumber-1);
    }
}
