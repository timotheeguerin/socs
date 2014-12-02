import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Elevator.
 * Created by tim on 14-12-02.
 */
class Elevator extends Process {
    private ElevatorEngine elevatorEngine;
    private List<Button> floorButtons;
    private List<Button> elevatorButtons;
    private List<Door> doors;
    int currentFloor = 0;
    Queue<Integer> queue = new ArrayDeque<>();
    List<List<Person>> personsForFloor = new ArrayList<>(ElevatorSimulator.FLOOR_COUNT);

    public Elevator(ElevatorEngine elevatorEngine, List<Button> floorButtons, List<Button> elevatorButtons, List<Door> doors) {
        for (int i = 0; i < ElevatorSimulator.FLOOR_COUNT; i++) {
            personsForFloor.add(new ArrayList<>());
        }
        this.elevatorEngine = elevatorEngine;
        this.floorButtons = floorButtons;
        this.elevatorButtons = elevatorButtons;
        this.doors = doors;
    }

    public void loop() throws InterruptedException {
        // Wait for an event from any of the buttons or the engine
        List<Process> process = new ArrayList<>(elevatorButtons);
        process.addAll(floorButtons);
        process.add(elevatorEngine);
        Pair<Process, Object> data = waitFrom(process);

        if (data.getKey() instanceof Button) { // If the event was sent by a button(elevator or floor)
            Pair pair = (Pair) data.getValue();
            Person person = (Person) pair.getKey();
            int floor = (int) pair.getValue();

            //Add the person to the floor it needs to go or the floor he is so the elevator can pick him up.
            personsForFloor.get(floor).add(person);
            if (queue.size() == 0) { // If the first request then we can ask the engine to run it.
                sendLock(elevatorEngine, floor);
            }

            if (!queue.contains(floor)) {
                queue.add(floor);
            }
        } else if (data.getKey() instanceof ElevatorEngine) { // If the event was sent by a the engine
            currentFloor = (int) data.getValue();
            //Elevator arrived at destination
            sendLock(doors.get(currentFloor), true);
            for (Person p : personsForFloor.get(currentFloor)) {
                sendLock(p, currentFloor);
                waitFrom(p); //Wait the user to enter or tell he is not on the floor
            }
            sendLock(doors.get(currentFloor), false);
            queue.remove();
            personsForFloor.get(currentFloor).clear();
            if (!queue.isEmpty()) {
                int nextFloor = queue.peek();
                sendLock(elevatorEngine, nextFloor);
            }
        }
    }

    public void setElevatorEngine(ElevatorEngine engine) {
        this.elevatorEngine = engine;
    }
}

