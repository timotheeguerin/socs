import javafx.util.Pair;

import java.util.List;

/**
 * Button
 * Created by tim on 14-12-02.
 */
class Button extends Process {
    private Elevator elevator;
    private List<Person> persons;

    public Button(int id, Elevator elevator, List<Person> persons) {
        this.id = id;
        this.persons = persons;
        this.elevator = elevator;
    }

    public void loop() throws InterruptedException {
        Pair<Process, Object> data = waitFrom(persons);
        boolean clicked = (boolean) data.getValue();
        if (clicked) {
            sendLock(elevator, new Pair<>(data.getKey(), id));
        }
    }
}
