import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Message pool
 * Created by tim on 14-12-01.
 */
public class MessagePool {
    private static Map<Pair<Process, Process>, Object> messages = new HashMap<>();

    public static synchronized boolean hasMessage(Process sender, Process destination) {
        return messages.get(new Pair<>(sender, destination)) != null;
    }

    public static synchronized void sendMessage(Process sender, Process destination, Object message) {
        messages.put(new Pair<>(sender, destination), message);
    }

    public static synchronized Object getMessage(Process sender, Process destination) {
        Pair<Process, Process> key = new Pair<>(sender, destination);
        Object message = messages.get(key);
        if (message != null) {
            messages.remove(key);
        }
        return message;
    }

    public static void print() {
        System.out.println("=====================================================");
        System.out.println("MESSAGE POOL:");
        for (Map.Entry<Pair<Process, Process>, Object> entry : messages.entrySet()) {
            Pair<Process, Process> key = entry.getKey();
            System.out.println("Sender: " + key.getKey().toString() + ", Dest: " +
                    key.getValue().toString() + ", message: " + entry.getValue());
        }
        System.out.println("-----------------------------------------------------");

    }
}
