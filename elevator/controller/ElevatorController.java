package controller;

import enums.Direction;
import enums.Status;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import model.Elevator;

public class ElevatorController {
    Elevator elevator;
    //While Going Up Fulfill all the request greater than current floor but minimum first
    PriorityQueue<Integer> minHeapWhileGoingUp;
    //While Going Down fulfill all the request smaller thant current floor but maximum first
    PriorityQueue<Integer> maxHeapWhileGoingDown;
    Queue<Integer> requestQueue;

    public ElevatorController(Elevator elevator) {
        this.elevator = elevator;
        minHeapWhileGoingUp = new PriorityQueue<>();
        maxHeapWhileGoingDown = new PriorityQueue<>(Collections.reverseOrder());
        requestQueue = new LinkedList<>();
    }

    public Elevator getElevator(){
        return elevator;
    }


    public void acceptHallCall(int destinationFloor, Direction destinationDirection){
        System.out.println("  [Controller-" + elevator.getCurrentId()
                + "] hall call: floor=" + destinationFloor + " dir=" + destinationDirection);
        addStop(destinationFloor);
        wakeIfIdle(destinationFloor);
        runUntilIdle();
    }

    public void acceptCarCall(int destinationFloor){
        System.out.println("  [Controller-" + elevator.getCurrentId()
                + "] car call: floor=" + destinationFloor);
        addStop(destinationFloor);
        wakeIfIdle(destinationFloor);
        runUntilIdle();
    }

    private void addStop(int floor) {
        int current = elevator.getCurrentFloor();
        if (floor == current) {
            System.out.println("    [Controller-" + elevator.getCurrentId()
                    + "] already at floor " + floor + ", doors open");
            return;
        }
        if (floor > current) {
            if (!minHeapWhileGoingUp.contains(floor)) minHeapWhileGoingUp.offer(floor);
        } else {
            if (!maxHeapWhileGoingDown.contains(floor)) maxHeapWhileGoingDown.offer(floor);
        }
    }

    public void step() {
        if (minHeapWhileGoingUp.isEmpty() && maxHeapWhileGoingDown.isEmpty()) {
            elevator.setCurrentStatus(Status.IDLE);
            return;
        }
        if (elevator.getCurrentDirection() == Direction.UP) {
            if (!minHeapWhileGoingUp.isEmpty()) {
                int target = minHeapWhileGoingUp.poll();
                elevator.moveElevator(target);
                System.out.println("    [Controller-" + elevator.getCurrentId() + "] doors open at " + target);
            } else {
                elevator.setCurrentDirection(Direction.DOWN);
            }
        } else { // DOWN
            if (!maxHeapWhileGoingDown.isEmpty()) {
                int target = maxHeapWhileGoingDown.poll();
                elevator.moveElevator(target);
                System.out.println("    [Controller-" + elevator.getCurrentId() + "] doors open at " + target);
            } else {
                elevator.setCurrentDirection(Direction.UP);
            }
        }
    }

    public void runUntilIdle() {
        while (!minHeapWhileGoingUp.isEmpty() || !maxHeapWhileGoingDown.isEmpty()) {
            step();
        }
        elevator.setCurrentStatus(Status.IDLE);
        System.out.println("  [Controller-" + elevator.getCurrentId()
                + "] now IDLE at floor " + elevator.getCurrentFloor());
    }



    private void wakeIfIdle(int targetFloor) {
        if (elevator.getCurrentStatus() == Status.IDLE) {
            elevator.setCurrentStatus(Status.MOVING);
            if (targetFloor > elevator.getCurrentFloor()) {
                elevator.setCurrentDirection(Direction.UP);
            } else if (targetFloor < elevator.getCurrentFloor()) {
                elevator.setCurrentDirection(Direction.DOWN);
            }
        }
    }

    public int calculateCost(int userFloor, Direction userDirection) {
        int elevatorFloor = elevator.getCurrentFloor();
        Direction elevatorDirection = elevator.getCurrentDirection();
        Status elevatorStatus = elevator.getCurrentStatus();

        if(elevatorStatus == Status.IDLE){
            return Math.abs(elevatorFloor - userFloor);
        }

        //Elevator is going up from 1, someone called the elevator to go up on 3rd floor
        if(elevatorDirection == Direction.UP && userDirection == Direction.UP && elevatorFloor <= userFloor){
            return Math.abs(userFloor-elevatorFloor);
        }
        //Elevator is going down from 3, someone called the elevator to go down on 2nd floor
        if(elevatorDirection == Direction.DOWN && userDirection == Direction.DOWN && elevatorFloor >= userFloor){
            return Math.abs(userFloor-elevatorFloor);
        }
        //Elevator is going up from 3, someone called the elevator to go from on 1st floor
        if(elevatorDirection == Direction.UP){
            int maxFloor = getFurthestStopAbove(elevatorFloor);
            int costUpwards = maxFloor-elevatorFloor;
            int costDownwards = maxFloor-userFloor;
            return costUpwards + costDownwards;
        }
        //Elevator is going down from 3, someone called the elevator to go from on 4th floor
        if(elevatorDirection == Direction.DOWN){
            int minFloor = getFurthestStopBelow(elevatorFloor);
            int costDownwards = elevatorFloor-minFloor;
            int costUpwards = userFloor-minFloor;
            return costUpwards + costDownwards;
        }
        return 0;
    }

    private int getFurthestStopAbove(int elevatorFloor) {
        if (minHeapWhileGoingUp.isEmpty()) return elevatorFloor;
        return Math.max(elevatorFloor, Collections.max(minHeapWhileGoingUp));
    }

    private int getFurthestStopBelow(int elevatorFloor) {
        if (maxHeapWhileGoingDown.isEmpty()) return elevatorFloor;
        return Math.min(elevatorFloor, Collections.min(maxHeapWhileGoingDown));
    }
    
    
}
