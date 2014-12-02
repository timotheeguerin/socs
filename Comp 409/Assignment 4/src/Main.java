import javafx.util.Pair;

import java.util.*;

/**
 * Main
 * Created by tim on 14-12-01.
 */
public class Main {
    private static final int FLOOR_COUNT = 3;

    private Elevator elevator = new Elevator();
    private ElevatorEngine elevatorEngine = new ElevatorEngine();

    private List<Button> elevatorButtons = new ArrayList<>(FLOOR_COUNT);

    private List<Button> floorButtons = new ArrayList<>(FLOOR_COUNT);

    private List<Door> doors = new ArrayList<>(FLOOR_COUNT);

    private List<Person> persons = new ArrayList<>();



    public static void main(String[] args) {
        new Main(2);
    }

    public Main(int personCount) {

        // Load, start and join all threads
        for (int i = 0; i < FLOOR_COUNT; i++) {
            elevatorButtons.add(new Button(i));
            floorButtons.add(new Button(i));
            doors.add(new Door(i));
        }
        for (int i = 0; i < personCount; i++) {
            persons.add(new Person(i));
        }

        elevator.start();
        elevatorEngine.start();

        for (int i = 0; i < FLOOR_COUNT; i++) {
            elevatorButtons.get(i).start();
            floorButtons.get(i).start();
            doors.get(i).start();
        }
        for (int i = 0; i < personCount; i++) {
            persons.get(i).start();
        }

        try {
            elevator.join();
            elevatorEngine.join();

            for (int i = 0; i < FLOOR_COUNT; i++) {
                elevatorButtons.get(i).join();
                floorButtons.get(i).join();
                doors.get(i).join();
            }
            for (int i = 0; i < personCount; i++) {
                persons.get(i).join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    class Elevator extends Process {
        int currentFloor = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        List<List<Person>> personsForFloor = new ArrayList<>(FLOOR_COUNT);

        public Elevator() {
            for (int i = 0; i < FLOOR_COUNT; i++) {
                personsForFloor.add(new ArrayList<>());
            }
        }

        public void run() {
            while (true) {
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
        }
    }

    class ElevatorEngine extends Process {
        public void move(int floor) {
            printAndSleep("Moving to " + floor);
        }

        public void run() {
            while (true) {
                int floor = (int) waitFrom(elevator);
                move(floor);
                sendLock(elevator, floor);
            }
        }
    }

    class Door extends Process {
        private int id;

        public Door(int id) {
            this.id = id;
        }

        private void open() {
            printAndSleep("Opening doors on floor " + id);
        }


        private void close() {
            printAndSleep("Closing doors on floor " + id);
        }

        public void run() {
            while (true) {
                boolean val = (boolean) waitFrom(elevator);
                if (val) {
                    open();
                } else {
                    close();
                }

            }
        }
    }

    class Button extends Process {

        public Button(int id) {
            this.id = id;
        }

        public void run() {
            while (true) {
                Pair<Process, Object> data = waitFrom(persons);
                boolean clicked = (boolean) data.getValue();
                if (clicked) {
                    sendLock(elevator, new Pair<Process, Integer>(data.getKey(), id));
                }
            }
        }
    }

    class Person extends Process {
        private int currentFloor = random.nextInt(FLOOR_COUNT);

        public Person(int id) {
            this.id = id;
        }

        private void enterElevator() {
            printAndSleep("Person " + id + " enter elevator on floor " + currentFloor);
        }

        private void leavingElevator() {
            printAndSleep("Person " + id + " leave elevator on floor " + currentFloor);
        }

        public void run() {
            while (true) {
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
                    destination = random.nextInt(FLOOR_COUNT);
                }

                //Tell the elevator the destination by pressing the elevator buttons
                sendLock(elevatorButtons.get(destination), true);

                //Wait again for the elevator to reach your destination
                waitFrom(elevator);
                currentFloor = destination;
                leavingElevator();

                // Tell the elevator we have left
                sendLock(elevator, true);
            }
        }
    }
}
