package echo.task;

import java.time.LocalDate;
import java.time.LocalTime;

import echo.util.DateTimeParser;

/**
 * Represents a task with a due date and optional due time.
 */
public class DeadlineTask extends Task {
    /** Due date stored as a Java date value. */
    private final LocalDate by;

    /** Optional due time stored as a Java time value. */
    private final LocalTime byTime;

    /**
     * Creates a deadline task.
     *
     * @param description Task description.
     * @param by Due date with an optional time.
     */
    public DeadlineTask(String description, String by) {
        super(description);
        DateTimeParser.DateTimeValue dateTime = DateTimeParser.parse(by);
        this.by = dateTime.date();
        this.byTime = dateTime.time();
    }

    /**
     * Returns this task in the format used by task-list persistence.
     *
     * @return Serialized deadline-task data.
     */
    @Override
    public String toFileFormat() {
        return "D | " + getDoneFlag() + " | " + getDescription() + " | "
                + DateTimeParser.formatForStorage(by, byTime);
    }

    /**
     * Returns the user-facing representation of this deadline task.
     *
     * @return Formatted deadline-task description.
     */
    @Override
    public String toString() {
        return "[Deadline]" + super.toString() + " (by: "
                + DateTimeParser.formatForDisplay(by, byTime) + ")";
    }
}
