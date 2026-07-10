package dispatcher;

import java.util.ArrayList;
import java.util.List;

import controller.ElevatorController;
import enums.Direction;
import model.Elevator;

public class Dispatcher {
    List<ElevatorController> elevatorControllers;

    public Dispatcher() {
        elevatorControllers = new ArrayList<>();
    }


    public void addController(ElevatorController elevatorController){
        elevatorControllers.add(elevatorController);
    }

    public Elevator handleHallCall(int floorNumber, Direction direction){
        ElevatorController bestController = null;
        int leastCost = Integer.MAX_VALUE;
        //Finding the best controller
        for(ElevatorController elevatorController : elevatorControllers){
            int currentCost = elevatorController.calculateCost(floorNumber, direction);
            if(currentCost < leastCost){
                leastCost = currentCost;
                bestController = elevatorController;
            }
        }
        bestController.acceptHallCall(floorNumber, direction);
        return bestController.getElevator();
    }

    
}
