import java.util.List;

/**
 * Represents an executable command entered by the user.
 */
public abstract class Command {
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

    /** The command type retained during the incremental migration. */
    private final Type type;

    /** The validated arguments retained during the incremental migration. */
    private final List<String> arguments;

    /**
     * Creates a command with already-validated arguments.
     *
     * @param type Command type.
     * @param arguments Validated command arguments.
     */
    protected Command(Type type, List<String> arguments) {
        this.type = type;
        this.arguments = List.copyOf(arguments);
    }

    /**
     * Executes this command using the application's task service and UI.
     *
     * @param taskManager Service used to perform task operations.
     * @param ui Interface used to display results.
     * @throws EchoException If the command cannot be completed.
     */
    public abstract void execute(TaskManager taskManager, Ui ui) throws EchoException;

    /**
     * Checks whether executing this command should end the session.
     *
     * @return Whether this command exits E.C.H.O.
     */
    public boolean isExit() {
        return false;
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
