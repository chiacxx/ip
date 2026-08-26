import java.util.List;

/**
 * Represents a validated command entered by the user.
 */
public final class Command {
    /**
     * Represents an action supported by E.C.H.O.
     */
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

    /** The validated action represented by this command. */
    private final Type type;

    /** The validated arguments supplied with this command. */
    private final List<String> arguments;

    /**
     * Creates a command with its already-validated arguments.
     *
     * @param type Command type.
     * @param arguments Command arguments.
     */
    public Command(Type type, List<String> arguments) {
        this.type = type;
        this.arguments = List.copyOf(arguments);
    }

    /**
     * Returns the command type.
     *
     * @return Command type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns a validated command argument.
     *
     * @param index Zero-based argument position.
     * @return Argument at that position.
     */
    public String getArgument(int index) {
        return arguments.get(index);
    }
}
