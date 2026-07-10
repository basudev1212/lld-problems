
import enums.Direction;
import model.Building;
import model.Elevator;
import model.Floor;

public class ElevatorApplication {
    public static void main(String[] args) {
        //Creating the floors and elevators
       System.out.println("=== Building setup ===");
        Building building = new Building();
        for (int i = 1; i <= 6; i++) 
            building.addFloor(i);
        building.addElevator(1);
        building.addElevator(2);
        System.out.println("6 floors, 2 elevators (both IDLE at floor 0)\n");
        System.out.println("=== Scenario 1: floor 3 presses DOWN ===");
        Floor floor3 = building.getFloor(3);
        Elevator e1 = floor3.pressButton(Direction.DOWN);
        System.out.println("[Main] assigned elevator " + e1.getCurrentId() + "\n");
        System.out.println("=== Scenario 2: rider in that elevator presses 1 ===");
        e1.getInternalButton().goToFloor(1);
        System.out.println();
        System.out.println("=== Scenario 3: floor 5 presses UP ===");
        Floor floor5 = building.getFloor(5);
        Elevator e2 = floor5.pressButton(Direction.UP);
        System.out.println("[Main] assigned elevator " + e2.getCurrentId() + "\n");
        System.out.println("=== Scenario 4: rider presses 6 ===");
        e2.getInternalButton().goToFloor(6);
        System.out.println();
        System.out.println("=== Scenario 5: floor 6 presses DOWN (someone is already there) ===");
        Floor floor6 = building.getFloor(6);
        Elevator e3 = floor6.pressButton(Direction.DOWN);
        System.out.println("[Main] assigned elevator " + e3.getCurrentId());
    }
    
}
