package echo.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores tasks in insertion order and provides one-based lookup.
 */
public class TaskList {
    /** The tasks currently in the user's list. */
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this(List.of());
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns a task using its one-based task number.
     *
     * @param taskNumber One-based task number.
     * @return The removed task.
     */
    public Task removeTask(int taskNumber) {
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns a task using its one-based task number.
     *
     * @param taskNumber One-based task number.
     * @return Task at that position.
     */
    public Task getTask(int taskNumber) {
        return tasks.get(taskNumber - 1);
    }

    /**
     * Checks whether a task number is available.
     *
     * @param taskNumber One-based task number.
     * @return Whether the task exists.
     */
    public boolean hasTask(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether the list contains no tasks.
     *
     * @return Whether the task list is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns a read-only snapshot of the tasks in insertion order.
     *
     * @return Immutable task snapshot.
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }
}
