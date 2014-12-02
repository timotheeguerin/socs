/**
 * Door.
 * Created by tim on 14-12-02.
 */
class Door extends Process {
    private Elevator elevator;
    private int id;

    public Door(int id, Elevator elevator) {
        this.id = id;
        this.elevator = elevator;
    }

    private void open() throws InterruptedException {
        printAndSleep("Opening doors on floor " + id);
    }


    private void close() throws InterruptedException {
        printAndSleep("Closing doors on floor " + id);
    }

    public void loop() throws InterruptedException {
        boolean val = (boolean) waitFrom(elevator);
        if (val) {
            open();
        } else {
            close();
        }
    }
}