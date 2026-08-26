import java.time.LocalDate;
import java.time.LocalTime;

/** Represents a Deadline Task. */
public class DeadlineTask extends Task {
    /** Deadline date stored as a Java date value. */
    private final LocalDate by;

    /** Optional deadline time stored as a Java time value. */
    private final LocalTime byTime;

    /**
     * Creates a deadline task.
     *
     * @param description task description
     * @param by deadline date with an optional time
     */
    public DeadlineTask(String description, String by) {
        super(description);
        DateTimeParser.DateTimeValue dateTime = DateTimeParser.parse(by);
        this.by = dateTime.date();
        this.byTime = dateTime.time();
    }

    @Override
    public String toFileFormat() {
        return "D | " + getDoneFlag() + " | " + getDescription() + " | "
                + DateTimeParser.formatForStorage(by, byTime);
    }

    @Override
    public String toString() {
        return "[Deadline]" + super.toString() + " (by: "
                + DateTimeParser.formatForDisplay(by, byTime) + ")";
    }
}
