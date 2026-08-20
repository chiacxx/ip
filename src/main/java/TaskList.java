import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks in insertion order and provides one-based lookup.
 */
public class TaskList {
    /** The tasks currently in the user's list. */
    private final List<Task> tasks = new ArrayList<>();

    /** Creates an empty task list. */
    public TaskList() {
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns a task using its one-based task number.
     *
     * @param taskNumber one-based task number
     * @return the removed task
     */
    public Task removeTask(int taskNumber) {
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns a task using its one-based task number.
     *
     * @param taskNumber one-based task number
     * @return task at that position
     */
    public Task getTask(int taskNumber) {
        return tasks.get(taskNumber - 1);
    }

    /**
     * Checks whether a task number is available.
     *
     * @param taskNumber one-based task number
     * @return whether the task exists
     */
    public boolean hasTask(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return number of stored tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether the list contains no tasks.
     *
     * @return whether the list is empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
