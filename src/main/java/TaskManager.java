/**
 * Provides the task operations used by E.C.H.O.
 */
public class TaskManager {
    /** The task list used to store and persist tasks. */
    private final TaskList taskList;

    /**
     * Creates a task manager using the default persistent task list.
     */
    public TaskManager() {
        this(new TaskList());
    }

    /**
     * Creates a task manager using the supplied task list.
     *
     * @param taskList Task list used by this manager.
     */
    public TaskManager(TaskList taskList) {
        this.taskList = taskList;
    }

    /**
     * Creates and stores a todo task.
     *
     * @param description Task description.
     * @return The new task.
     */
    public Task addTodo(String description) {
        return addTask(new TodoTask(description));
    }

    /**
     * Creates and stores a deadline task.
     *
     * @param description Task description.
     * @param by Due date with an optional time.
     * @return The new task.
     */
    public Task addDeadline(String description, String by) {
        return addTask(new DeadlineTask(description, by));
    }

    /**
     * Creates and stores an event task.
     *
     * @param description Event description.
     * @param from Event start date with an optional time.
     * @param to Event end date with an optional time.
     * @return The new task.
     */
    public Task addEvent(String description, String from, String to) {
        return addTask(new EventTask(description, from, to));
    }

    /**
     * Returns a task at a one-based task-list position.
     *
     * @param taskNumber One-based task number.
     * @return Task at that position.
     */
    public Task getTask(int taskNumber) {
        return taskList.getTask(taskNumber);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return Number of tasks.
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Checks whether no tasks have been added.
     *
     * @return Whether the task list is empty.
     */
    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    /**
     * Marks a task as done.
     *
     * @param taskNumber One-based task number.
     * @return The marked task.
     * @throws EchoException If the task number is unavailable.
     */
    public Task markTask(int taskNumber) throws EchoException {
        Task task = requireTask(taskNumber);
        task.mark();
        taskList.save();
        return task;
    }

    /**
     * Marks a task as not done.
     *
     * @param taskNumber One-based task number.
     * @return The unmarked task.
     * @throws EchoException If the task number is unavailable.
     */
    public Task unmarkTask(int taskNumber) throws EchoException {
        Task task = requireTask(taskNumber);
        task.unmark();
        taskList.save();
        return task;
    }

    /**
     * Removes a task from the task list.
     *
     * @param taskNumber One-based task number.
     * @return The removed task.
     * @throws EchoException If the task number is unavailable.
     */
    public Task deleteTask(int taskNumber) throws EchoException {
        requireTask(taskNumber);
        return taskList.removeTask(taskNumber);
    }

    /** Adds a task and returns it for display by the user interface. */
    private Task addTask(Task task) {
        taskList.addTask(task);
        return task;
    }

    /** Retrieves a task after checking that its one-based number exists. */
    private Task requireTask(int taskNumber) throws EchoException {
        if (!taskList.hasTask(taskNumber)) {
            throw new EchoException("There is no task numbered " + taskNumber
                    + ". Use 'list' to see the available task numbers.");
        }
        return taskList.getTask(taskNumber);
    }
}
