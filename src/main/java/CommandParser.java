import java.util.List;
import java.util.Locale;

/**
 * Parses raw user input into validated Command objects.
 */
public class CommandParser {
    private static final String TODO_FORMAT = "Try: todo <description>.";
    private static final String DEADLINE_FORMAT = "Try: deadline <description> /by <deadline>.";
    private static final String EVENT_FORMAT = "Try: event <description> /from <start> /to <end>.";


    /**
     * Parses one line of user input.
     *
     * @param input Raw input line
     * @return Validated command
     * @throws EchoException if the input is not a supported command
     */
    public Command parse(String input) throws EchoException {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new EchoException("Please enter a command. Try 'list' to view your tasks.");
        }

        String[] commandParts = trimmedInput.split("\\s+");
        String commandName = commandParts[0].toLowerCase(Locale.ROOT);

        return switch (commandName) {
            case "help" -> parseSimpleCommand(commandParts, Command.Type.HELP,
                    "'help' does not take any arguments. Type 'help' to view list of operations.");
            case "list" -> parseSimpleCommand(commandParts, Command.Type.LIST,
                    "'list' does not take any arguments. Type 'list' to view all your tasks.");
            case "todo" -> parseTodo(trimmedInput);
            case "deadline" -> parseDeadline(trimmedInput);
            case "event" -> parseEvent(trimmedInput);
            case "mark" -> parseStatusCommand(commandParts, Command.Type.MARK);
            case "unmark" -> parseStatusCommand(commandParts, Command.Type.UNMARK);
            case "bye" -> parseSimpleCommand(commandParts, Command.Type.BYE,
                    "'bye' does not take any arguments. Type 'bye' when you are ready to disconnect.");
            default -> throw new EchoException("I do not recognise '" + commandName
                    + "'. Try 'help' to see the available commands.");
        };
    }

    /** Parses a command that accepts no arguments. */
    private Command parseSimpleCommand(String[] commandParts, Command.Type type,
                                       String errorMessage) throws EchoException {
        if (commandParts.length != 1) {
            throw new EchoException(errorMessage);
        }

        return new Command(type, List.of());
    }

    /** Parses a todo and keeps its complete description as one argument. */
    private Command parseTodo(String input) throws EchoException {
        String description = getCommandContent(input);
        if (description.isEmpty()) {
            throw new EchoException("A todo needs a description. " + TODO_FORMAT);
        }
        return new Command(Command.Type.TODO, List.of(description));
    }

    /** Parses a deadline in the form 'deadline <description> /by <deadline>'. */
    private Command parseDeadline(String input) throws EchoException {
        String content = getCommandContent(input);
        if (content.isEmpty()) {
            throw new EchoException("A deadline needs a description and a due date. "
                    + DEADLINE_FORMAT);
        }

        if (!containsMarker(content, "/by")) {
            throw new EchoException("A deadline must include a due date using '/by <deadline>'. "
                    + DEADLINE_FORMAT);
        }

        String[] parts = content.split("(?i)\\s+/by\\s+", 2);
        if (parts.length == 1) {
            if (content.toLowerCase(Locale.ROOT).startsWith("/by")) {
                throw new EchoException("A deadline needs a description before '/by'. "
                        + DEADLINE_FORMAT);
            }
            throw new EchoException("Please provide a due date after '/by'. "
                    + DEADLINE_FORMAT);
        }

        if (parts[0].isBlank()) {
            throw new EchoException("A deadline needs a description before '/by'. "
                    + DEADLINE_FORMAT);
        }
        if (parts[1].isBlank()) {
            throw new EchoException("Please provide a due date after '/by'. "
                    + DEADLINE_FORMAT);
        }

        return new Command(Command.Type.DEADLINE,
                List.of(parts[0].trim(), parts[1].trim()));
    }

    /** Parses an event in the form 'event <description> /from <start> /to <end>'. */
    private Command parseEvent(String input) throws EchoException {
        String content = getCommandContent(input);
        if (content.isEmpty()) {
            throw new EchoException("An event needs a description, start, and end time. " + EVENT_FORMAT);
        }

        if (!containsMarker(content, "/from")) {
            throw new EchoException("An event must include a start time using '/from <start>'. " + EVENT_FORMAT);
        }

        String[] partsFrom = content.split("(?i)\\s+/from\\s+", 2);
        if (partsFrom.length < 2) {
            throw new EchoException("Please provide a start time after '/from'. " + EVENT_FORMAT);
        }
        if (partsFrom[0].isBlank()) {
            throw new EchoException("An event needs a description before '/from'. " + EVENT_FORMAT);
        }

        if (!containsMarker(partsFrom[1], "/to")) {
            throw new EchoException("An event must include an end time using '/to <end>'. " + EVENT_FORMAT);
        }

        String[] partsTo = partsFrom[1].split("(?i)\\s+/to\\s+", 2);
        if (partsTo.length < 2) {
            if (partsFrom[1].trim().toLowerCase(Locale.ROOT).startsWith("/to")) {
                throw new EchoException("Please provide a start time after '/from'. " + EVENT_FORMAT);
            }
            throw new EchoException("Please provide an end time after '/to'. " + EVENT_FORMAT);
        }
        if (partsTo[0].isBlank()) {
            throw new EchoException("Please provide a start time after '/from'. " + EVENT_FORMAT);
        }
        if (partsTo[1].isBlank()) {
            throw new EchoException("Please provide an end time after '/to'. " + EVENT_FORMAT);
        }

        return new Command(Command.Type.EVENT,
                List.of(partsFrom[0].trim(), partsTo[0].trim(), partsTo[1].trim()));
    }

    /** Parses a mark or unmark command and validates its task number syntax. */
    private Command parseStatusCommand(String[] commandParts, Command.Type type) throws EchoException {
        String commandName = type == Command.Type.MARK ? "mark" : "unmark";
        if (commandParts.length != 2) {
            throw new EchoException("Please provide exactly one task number. Use '"
                    + commandName + " <number>'.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(commandParts[1]);
        } catch (NumberFormatException exception) {
            throw new EchoException("'" + commandParts[1] + "' is not a valid task number. "
                    + "Use the number shown by 'list'.");
        }

        if (taskNumber < 1) {
            throw new EchoException("Task numbers start at 1. Use the number shown by 'list'.");
        }

        return new Command(type, List.of(String.valueOf(taskNumber)));
    }

    /** Checks for a field marker such as /by, ignoring letter case. */
    private static boolean containsMarker(String content, String marker) {
        return content.toLowerCase(Locale.ROOT).contains(marker.toLowerCase(Locale.ROOT));
    }

    /** Extracts the text following the first whitespace-separated command. */
    private String getCommandContent(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (Character.isWhitespace(input.charAt(i))) {
                return input.substring(i + 1).trim();
            }
        }
        return "";
    }
}
