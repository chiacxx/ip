/** Represents an Event Task. */
public class EventTask extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description event description
     * @param from event start date with an optional time
     * @param to event end date with an optional time
     */
    public EventTask(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileFormat() {
        return "E | " + getDoneFlag() + " | " + getDescription() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[Event]" + super.toString() + " (from: "
                + DateTimeParser.formatForDisplay(this.from) + ", to: "
                + DateTimeParser.formatForDisplay(this.to) + ")";
    }
}
