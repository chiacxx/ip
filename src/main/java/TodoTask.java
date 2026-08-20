/** Represents a Todo Task. */
public class TodoTask extends Task {
    public TodoTask(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[To-Do]" + super.toString();
    }
}
