/** Represents a Deadline Task. */
public class DeadlineTask extends Task {
    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description task description
     * @param by deadline date with an optional time
     */
    public DeadlineTask(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toFileFormat() {
        return "D | " + getDoneFlag() + " | " + getDescription() + " | " + by;
    }

    @Override
    public String toString() {
        return "[Deadline]" + super.toString() + " (by: "
                + DateTimeParser.formatForDisplay(this.by) + ")";
    }
}
