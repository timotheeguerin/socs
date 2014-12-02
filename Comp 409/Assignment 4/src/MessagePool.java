import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Message pool
 * Created by tim on 14-12-01.
 */
public class MessagePool {
    /**
     * Pool of message.
     * Key: (sender, destination)
     * Value: Message
     */
    private static Map<Pair<Process, Process>, Object> messages = new HashMap<>();

    /**
     * Check if the given sender is currently sending a message to the given destination
     *
     * @param sender      Message source
     * @param destination Message destination
     * @return boolean if the to params are in the pool
     */
    public static synchronized boolean hasMessage(Process sender, Process destination) {
        return messages.get(new Pair<>(sender, destination)) != null;
    }

    /**
     * Send a message from sender to the destination
     *
     * @param sender      Message source
     * @param destination Message destination
     * @param message     Message to send
     */
    public static synchronized void sendMessage(Process sender, Process destination, Object message) {
        messages.put(new Pair<>(sender, destination), message);
    }

    /**
     * Try to read a message from sender to destination
     * If a message is found we remove it from the pool and return it.
     * Otherwise just return null.
     *
     * @param sender      Message source
     * @param destination Message destination
     * @return the message or null if no message found
     */
    public static synchronized Object getMessage(Process sender, Process destination) {
        Pair<Process, Process> key = new Pair<>(sender, destination);
        Object message = messages.get(key);
        if (message != null) {
            messages.remove(key);
        }
        return message;
    }

    /**
     * Helper method
     */
    @SuppressWarnings("unused")
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
