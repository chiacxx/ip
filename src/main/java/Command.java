import java.util.List;

/**
 * Represents a validated command entered by the user.
 */
public final class Command {
    /** The command actions supported by E.C.H.O. */
    public enum Type {
        /** Displays command guidance. */
        HELP,
        /** Displays all tasks. */
        LIST,
        /** Adds a todo task. */
        TODO,
        /** Adds a deadline task. */
        DEADLINE,
        /** Adds an event task. */
        EVENT,
        /** Marks a task as done. */
        MARK,
        /** Marks a task as not done. */
        UNMARK,
        /** Deletes a task. */
        DELETE,
        /** Ends the session. */
        BYE
    }

    private final Type type;
    private final List<String> arguments;

    /**
     * Creates a command with its already-validated arguments.
     *
     * @param type command type
     * @param arguments command arguments
     */
    public Command(Type type, List<String> arguments) {
        this.type = type;
        this.arguments = List.copyOf(arguments);
    }

    /**
     * Returns the command type.
     *
     * @return command type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns a validated command argument.
     *
     * @param index zero-based argument position
     * @return argument at that position
     */
    public String getArgument(int index) {
        return arguments.get(index);
    }
}
