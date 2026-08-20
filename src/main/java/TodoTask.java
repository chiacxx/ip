/** Represents a Todo Task. */
public class TodoTask extends Task {
    /**
     * Creates a todo task.
     *
     * @param description task description
     */
    public TodoTask(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[To-Do]" + super.toString();
    }
}
