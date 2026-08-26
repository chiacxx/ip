package echo.task;

/**
 * Represents a task that does not have a deadline or event time.
 */
public class TodoTask extends Task {
    /**
     * Creates a todo task.
     *
     * @param description Task description.
     */
    public TodoTask(String description) {
        super(description);
    }

    /**
     * Returns the user-facing representation of this todo task.
     *
     * @return Formatted todo-task description.
     */
    @Override
    public String toString() {
        return "[To-Do]" + super.toString();
    }
}
