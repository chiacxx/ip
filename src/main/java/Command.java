import java.util.List;

/**
 * Represents a validated command entered by the user.
 */
public final class Command {
    /** The command actions supported by E.C.H.O. */
    public enum Type {
        HELP,
        LIST,
        TODO,
        DEADLINE,
        EVENT,
        MARK,
        UNMARK,
        BYE
    }

    private final Type type;
    private final List<String> arguments;

    /** Creates a command with its already-validated arguments. */
    public Command(Type type, List<String> arguments) {
        this.type = type;
        this.arguments = List.copyOf(arguments);
    }

    public Type getType() {
        return type;
    }

    public String getArgument(int index) {
        return arguments.get(index);
    }
}
