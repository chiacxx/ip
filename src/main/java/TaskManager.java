/**
 * Provides the task operations used by E.C.H.O.
 */
public class TaskManager {
    private final TaskList taskList;

    /** Creates an empty task manager. */
    public TaskManager() {
        this.taskList = new TaskList();
    }

    /**
     * Creates and stores a todo task.
     *
     * @param description task description
     * @return the new task
     */
    public Task addTodo(String description) {
        return addTask(new TodoTask(description));
    }

    /**
     * Creates and stores a deadline task.
     *
     * @param description task description
     * @param by deadline text
     * @return the new task
     */
    public Task addDeadline(String description, String by) {
        return addTask(new DeadlineTask(description, by));
    }

    /**
     * Creates and stores an event task.
     *
     * @param description event description
     * @param from event start time
     * @param to event end time
     * @return the new task
     */
    public Task addEvent(String description, String from, String to) {
        return addTask(new EventTask(description, from, to));
    }

    /**
     * Returns a task at a one-based task-list position.
     *
     * @param taskNumber one-based task number
     * @return task at that position
     */
    public Task getTask(int taskNumber) {
        return taskList.getTask(taskNumber);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return number of tasks
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Checks whether no tasks have been added.
     *
     * @return whether the task list is empty
     */
    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    /**
     * Marks a task as done.
     *
     * @param taskNumber one-based task number
     * @return the marked task
     * @throws EchoException if the task number is unavailable
     */
    public Task markTask(int taskNumber) throws EchoException {
        Task task = requireTask(taskNumber);
        task.mark();
        return task;
    }

    /**
     * Marks a task as not done.
     *
     * @param taskNumber one-based task number
     * @return the unmarked task
     * @throws EchoException if the task number is unavailable
     */
    public Task unmarkTask(int taskNumber) throws EchoException {
        Task task = requireTask(taskNumber);
        task.unmark();
        return task;
    }

    /**
     * Removes a task from the task list.
     *
     * @param taskNumber one-based task number
     * @return the removed task
     * @throws EchoException if the task number is unavailable
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
