import java.util.*;

/**
 * Main
 * Created by tim on 14-12-01.
 */
public class ElevatorSimulator {

    public static final int FLOOR_COUNT = 3;
    private int personCount;
    private int passengerTripCount;

    private Elevator elevator;
    private ElevatorEngine elevatorEngine = new ElevatorEngine(elevator);

    private List<Button> elevatorButtons = new ArrayList<>(FLOOR_COUNT);

    private List<Button> floorButtons = new ArrayList<>(FLOOR_COUNT);

    private List<Door> doors = new ArrayList<>(FLOOR_COUNT);

    private List<Person> persons = new ArrayList<>();


    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error, expecting 2 arguments: n t");
        }
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        new ElevatorSimulator(n, t);
    }

    public ElevatorSimulator(int personCount, int passengerTripCount) {
        this.personCount = personCount;
        this.passengerTripCount = passengerTripCount;

        initProcess();
        startProcess();
        waitForPassengerToEnd();
        // Each person now have done all their trips
        terminateInfiniteProcess();
        System.out.println("All trips completed!");
    }

    private void initProcess() {
        elevator = new Elevator(elevatorEngine, floorButtons, elevatorButtons, doors);
        elevatorEngine = new ElevatorEngine(elevator);
        elevator.setElevatorEngine(elevatorEngine);

        for (int i = 0; i < FLOOR_COUNT; i++) {
            elevatorButtons.add(new Button(i, elevator, persons));
            floorButtons.add(new Button(i, elevator, persons));
            doors.add(new Door(i, elevator));
        }
        for (int i = 0; i < personCount; i++) {
            persons.add(new Person(i, passengerTripCount, elevator, floorButtons, elevatorButtons));
        }
    }

    private void startProcess() {
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
    }

    private void waitForPassengerToEnd() {
        try {
            for (int i = 0; i < personCount; i++) {
                persons.get(i).join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void terminateInfiniteProcess() {
        elevator.interrupt();
        elevatorEngine.interrupt();
        for (int i = 0; i < FLOOR_COUNT; i++) {
            elevatorButtons.get(i).interrupt();
            floorButtons.get(i).interrupt();
            doors.get(i).interrupt();
        }
    }


}
