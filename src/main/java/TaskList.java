import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks in insertion order and provides one-based lookup.
 */
public class TaskList {
    /** The tasks currently in the user's list. */
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task getTask(int taskNumber) {
        return tasks.get(taskNumber - 1);
    }

    public boolean hasTask(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
