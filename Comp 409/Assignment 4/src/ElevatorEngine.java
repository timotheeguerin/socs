/**
 * Elevator engine.
 * Created by tim on 14-12-02.
 */
class ElevatorEngine extends Process {
    private Elevator elevator;

    public ElevatorEngine(Elevator elevator) {
        this.elevator = elevator;
    }

    public void move(int floor) throws InterruptedException {
        printAndSleep("Moving to " + floor);
    }

    public void loop() throws InterruptedException {
        int floor = (int) waitFrom(elevator);
        move(floor);
        sendLock(elevator, floor);
    }
}