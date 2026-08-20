/** Represents an Event Task. */
public class EventTask extends Task {
    private final String from;
    private final String to;

    public EventTask(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[Event]" + super.toString() + " (from: " + this.from + ", to: " + this.to + ")";
    }
}
