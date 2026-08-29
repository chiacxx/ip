package echo.task;

import java.time.LocalDate;
import java.time.LocalTime;

import echo.util.DateTimeParser;

/**
 * Represents a task with a start date and an end date.
 */
public class EventTask extends Task {
    /** Event start date stored as a Java date value. */
    private final LocalDate from;

    /** Optional event start time stored as a Java time value. */
    private final LocalTime fromTime;

    /** Event end date stored as a Java date value. */
    private final LocalDate to;

    /** Optional event end time stored as a Java time value. */
    private final LocalTime toTime;

    /**
     * Creates an event task.
     *
     * @param description event description.
     * @param from event start date with an optional time.
     * @param to event end date with an optional time.
     */
    public EventTask(String description, String from, String to) {
        super(description);
        DateTimeParser.DateTimeValue fromDateTime = DateTimeParser.parse(from);
        DateTimeParser.DateTimeValue toDateTime = DateTimeParser.parse(to);
        this.from = fromDateTime.date();
        this.fromTime = fromDateTime.time();
        this.to = toDateTime.date();
        this.toTime = toDateTime.time();
    }

    @Override
    public String toFileFormat() {
        return "E | " + getDoneFlag() + " | " + getDescription() + " | "
                + DateTimeParser.formatForStorage(from, fromTime) + " | "
                + DateTimeParser.formatForStorage(to, toTime);
    }

    @Override
    public String toString() {
        return "[Event]" + super.toString() + " (from: "
                + DateTimeParser.formatForDisplay(from, fromTime) + ", to: "
                + DateTimeParser.formatForDisplay(to, toTime) + ")";
    }
}
