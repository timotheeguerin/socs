import java.util.List;

/**
 * Person
 * Created by tim on 14-12-02.
 */
class Person extends Process {
    private Elevator elevator;
    private List<Button> floorButtons;
    private List<Button> elevatorButtons;

    private int currentFloor = random.nextInt(ElevatorSimulator.FLOOR_COUNT);
    private int tripCount = 0;

    public Person(int id, int tripCount, Elevator elevator, List<Button> floorButtons, List<Button> elevatorButtons) {
        this.id = id;
        this.tripCount = tripCount;
        this.elevator = elevator;
        this.floorButtons = floorButtons;
        this.elevatorButtons = elevatorButtons;
    }

    private void enterElevator() throws InterruptedException {
        printAndSleep("Person " + id + " enter elevator on floor " + currentFloor);
    }

    private void leavingElevator() throws InterruptedException {
        printAndSleep("Person " + id + " leave elevator on floor " + currentFloor);
    }

    public void loop() throws InterruptedException {
        if (tripCount == 0) {
            throw new InterruptedException();
        }
        //Call the elevator by pressing the button
        Button floorButton = floorButtons.get(currentFloor);
        sendLock(floorButton, true);

        // Wait for the elevator
        waitFrom(elevator);

        enterElevator();
        //Tell the elevator we have entered
        sendLock(elevator, true);

        //Generate a random destination different from the current one
        int destination = currentFloor;
        while (destination == currentFloor) {
            destination = random.nextInt(ElevatorSimulator.FLOOR_COUNT);
        }

        //Tell the elevator the destination by pressing the elevator buttons
        sendLock(elevatorButtons.get(destination), true);

        //Wait again for the elevator to reach your destination
        waitFrom(elevator);
        currentFloor = destination;
        leavingElevator();

        // Tell the elevator we have left
        sendLock(elevator, true);
        tripCount--;
    }
}