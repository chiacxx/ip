import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

/**
 * Converts raw user input into validated {@link Command} objects.
 */
public class CommandParser {
    /** Usage guidance for todo commands. */
    private static final String TODO_FORMAT = "Try: todo <description>.";
    /** Placeholder text for a date and optional time in usage guidance. */
    private static final String DATE_TIME_ARGUMENT = "<" + DateTimeParser.DATE_FORMAT + "> ["
            + DateTimeParser.TIME_FORMAT + "]";
    /** Usage guidance for deadline commands. */
    private static final String DEADLINE_FORMAT = "Try: deadline <description> /by "
            + DATE_TIME_ARGUMENT + ".";
    /** Usage guidance for event commands. */
    private static final String EVENT_FORMAT = "Try: event <description> /from "
            + DATE_TIME_ARGUMENT + " /to " + DATE_TIME_ARGUMENT + ".";

    /**
     * Creates a parser for E.C.H.O. commands.
     */
    public CommandParser() {
    }

    /**
     * Parses one line of user input.
     *
     * @param input Raw input line.
     * @return Validated command.
     * @throws EchoException If the input is not a supported command.
     */
    public Command parse(String input) throws EchoException {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new EchoException("Please enter a command. Try 'list' to view your tasks.");
        }

        String[] commandParts = trimmedInput.split("\\s+");
        String commandName = commandParts[0].toLowerCase(Locale.ROOT);

        return switch (commandName) {
            case "help" -> parseNoArgumentCommand(commandParts, Command.Type.HELP,
                    "'help' does not take any arguments. Type 'help' to view list of operations.");
            case "list" -> parseNoArgumentCommand(commandParts, Command.Type.LIST,
                    "'list' does not take any arguments. Type 'list' to view all your tasks.");
            case "todo" -> parseTodoCommand(trimmedInput);
            case "deadline" -> parseDeadlineCommand(trimmedInput);
            case "event" -> parseEventCommand(trimmedInput);
            case "mark" -> parseTaskNumberCommand(commandParts, Command.Type.MARK);
            case "unmark" -> parseTaskNumberCommand(commandParts, Command.Type.UNMARK);
            case "delete" -> parseTaskNumberCommand(commandParts, Command.Type.DELETE);
            case "bye" -> parseExitCommand(commandParts);
            default -> throw new EchoException("I do not recognise '" + commandName
                    + "'. Try 'help' to see the available commands.");
        };
    }

    /** Parses a command that does not accept arguments. */
    private Command parseNoArgumentCommand(String[] commandParts, Command.Type type,
                                           String errorMessage) throws EchoException {
        if (commandParts.length != 1) {
            throw new EchoException(errorMessage);
        }

        return switch (type) {
        case HELP -> new HelpCommand();
        case LIST -> new ListCommand();
        default -> new ParsedCommand(type, List.of());
        };
    }

    /** Parses the command that ends the current session. */
    private Command parseExitCommand(String[] commandParts) throws EchoException {
        if (commandParts.length != 1) {
            throw new EchoException("'bye' does not take any arguments. "
                    + "Type 'bye' when you are ready to disconnect.");
        }

        return new ExitCommand();
    }

    /** Parses a task command with one task-number argument. */
    private Command parseTaskNumberCommand(String[] commandParts, Command.Type type)
            throws EchoException {
        String commandName = switch (type) {
            case MARK -> "mark";
            case UNMARK -> "unmark";
            case DELETE -> "delete";
            default -> throw new IllegalArgumentException("Unsupported task command: " + type);
        };
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

        return switch (type) {
        case MARK -> new MarkCommand(taskNumber);
        case UNMARK -> new UnmarkCommand(taskNumber);
        case DELETE -> new DeleteCommand(taskNumber);
        default -> throw new IllegalArgumentException("Unsupported task command: " + type);
        };
    }

    /** Parses a todo command and keeps its complete description as one argument. */
    private Command parseTodoCommand(String input) throws EchoException {
        String description = getCommandContent(input);
        if (description.isEmpty()) {
            throw new EchoException("A todo needs a description. " + TODO_FORMAT);
        }
        return new ParsedCommand(Command.Type.TODO, List.of(description));
    }

    /** Parses a deadline command with a date and optional time after {@code /by}. */
    private Command parseDeadlineCommand(String input) throws EchoException {
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

        String description = parts[0].trim();
        String dueDate = parts[1].trim();
        validateDateTime(dueDate, "deadline", DEADLINE_FORMAT);

        return new ParsedCommand(Command.Type.DEADLINE, List.of(description, dueDate));
    }

    /** Parses an event command with dates and optional times after {@code /from} and {@code /to}. */
    private Command parseEventCommand(String input) throws EchoException {
        String content = getCommandContent(input);
        if (content.isEmpty()) {
            throw new EchoException("An event needs a description, start date, and end date. " + EVENT_FORMAT);
        }

        if (!containsMarker(content, "/from")) {
            throw new EchoException("An event must include a start date using '/from <date>'. " + EVENT_FORMAT);
        }

        String[] partsFrom = content.split("(?i)\\s+/from\\s+", 2);
        if (partsFrom.length < 2) {
            throw new EchoException("Please provide a start date after '/from'. " + EVENT_FORMAT);
        }
        if (partsFrom[0].isBlank()) {
            throw new EchoException("An event needs a description before '/from'. " + EVENT_FORMAT);
        }

        if (!containsMarker(partsFrom[1], "/to")) {
            throw new EchoException("An event must include an end date using '/to <date>'. " + EVENT_FORMAT);
        }

        String[] partsTo = partsFrom[1].split("(?i)\\s+/to\\s+", 2);
        if (partsTo.length < 2) {
            if (partsFrom[1].trim().toLowerCase(Locale.ROOT).startsWith("/to")) {
                throw new EchoException("Please provide a start date after '/from'. " + EVENT_FORMAT);
            }
            throw new EchoException("Please provide an end date after '/to'. " + EVENT_FORMAT);
        }
        if (partsTo[0].isBlank()) {
            throw new EchoException("Please provide a start date after '/from'. " + EVENT_FORMAT);
        }
        if (partsTo[1].isBlank()) {
            throw new EchoException("Please provide an end date after '/to'. " + EVENT_FORMAT);
        }

        String description = partsFrom[0].trim();
        String startDate = partsTo[0].trim();
        String endDate = partsTo[1].trim();
        validateDateTime(startDate, "event start", EVENT_FORMAT);
        validateDateTime(endDate, "event end", EVENT_FORMAT);

        return new ParsedCommand(Command.Type.EVENT, List.of(description, startDate, endDate));
    }

    /** Validates a date with an optional time and reports the expected format on failure. */
    private static void validateDateTime(String date, String fieldName, String commandFormat)
            throws EchoException {
        try {
            DateTimeParser.validate(date);
        } catch (DateTimeParseException exception) {
            if (!date.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}( [0-9]{2}:[0-9]{2})?")) {
                throw new EchoException("The " + fieldName + " must use "
                        + DateTimeParser.DATE_FORMAT + " with an optional 24-hour time "
                        + DateTimeParser.TIME_FORMAT + ". " + commandFormat);
            }

            throw new EchoException("'" + date + "' is not a valid date or time. "
                    + "Please use " + DateTimeParser.DATE_TIME_FORMAT
                    + " when including a time. " + commandFormat);
        }
    }


    /** Checks for a field marker such as {@code /by}, ignoring letter case. */
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
