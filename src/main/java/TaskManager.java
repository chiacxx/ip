/**
 * Provides the task operations used by E.C.H.O.
 */
public class TaskManager {
    private final TaskList taskList;

    /** Creates an empty task manager. */
    public TaskManager() {
        this.taskList = new TaskList();
    }

    /** Creates and stores Todo Task. */
    public Task addTodo(String description) {
        return addTask(new TodoTask(description));
    }

    /** Creates and stores Deadline Task. */
    public Task addDeadline(String description, String by) {
        return addTask(new DeadlineTask(description, by));
    }

    /** Creates and stores Event Task. */
    public Task addEvent(String description, String from, String to) {
        return addTask(new EventTask(description, from, to));
    }

    /** Returns a task at a one-based task-list position. */
    public Task getTask(int taskNumber) {
        return taskList.getTask(taskNumber);
    }

    public int size() {
        return taskList.size();
    }

    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    /** Marks a task as done. */
    public Task markTask(int taskNumber) throws EchoException {
        Task task = requireTask(taskNumber);
        task.mark();
        return task;
    }

    /** Marks a task as undone. */
    public Task unmarkTask(int taskNumber) throws EchoException {
        Task task = requireTask(taskNumber);
        task.unmark();
        return task;
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
