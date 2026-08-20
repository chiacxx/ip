import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * E.C.H.O. - Everyday Conversational & Helpful Operator.
 */
public class Echo {
    private static final String SEPARATOR = "─────────────────────────────────────────────────────────────────────────────────";
    private static final String PROMPT = "E.C.H.O. ❯ ";
    private static final String RESPONSE_INDENT = "  ";
    private static final int RESPONSE_CONTENT_WIDTH = SEPARATOR.length() - RESPONSE_INDENT.length();

    private static final String HELP_MESSAGE = """
            Available operations:
              help                                         Show this help message
              list                                         Display all tasks
              todo <description>                           Add a todo task
              deadline <description> /by <deadline>        Add a deadline task
              event <description> /from <start> /to <end>  Add an event task
              mark <number>                                Mark a task as done
              unmark <number>                              Mark a task as not done
              bye                                          Disconnect from E.C.H.O.

            Task numbers are shown by the 'list' command.""";

    private static final String BANNER = """
       ______ _____ _   _  ____  \s
      |  ____/ ____| | | |/ __ \\ \s
      | |__ | |    | |_| | |  | |
      |  __|| |    |  _  | |  | |
      | |___| |____| | | | |__| |
      |______\\_____|_| |_|\\____/ \s
      """;

    private static final List<String> FAREWELL_FLAVORS = List.of(
            "Signal fading... E.C.H.O. signing off. Take care!",
            "Powering down the transmitter. Catch you soon!",
            "Going dark now. Thanks for the chat!"
    );

    private static final Random RANDOM = new Random();

    private final CommandParser commandParser;
    private final TaskManager taskManager;

    /** Creates a new E.C.H.O. session with an empty task list. */
    public Echo() {
        this.commandParser = new CommandParser();
        this.taskManager = new TaskManager();
    }

    /** Main driver to start an Echo session. */
    public static void main(String[] args) {
        new Echo().run();
    }

    /** Reads and processes inputs until the user disconnects or input ends. */
    private void run() {
        System.out.println(BANNER);
        printBotResponse("Signal established. Online and listening!\nType 'help' to view list of operations!");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print(PROMPT);

                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    continue;
                }

                try {
                    if (execute(commandParser.parse(input))) {
                        break;
                    }
                } catch (EchoException exception) {
                    printBotResponse(exception.getMessage());
                }
            }
        }
    }

    /**
     * Executes a validated command.
     *
     * @param command Validated command
     * @return Whether the session should end
     * @throws EchoException if the command refers to a task that does not exist
     */
    private boolean execute(Command command) throws EchoException {
        switch (command.getType()) {
            case HELP:
                handleHelp();
                return false;
            case LIST:
                handleList();
                return false;
            case TODO:
                announceAdded(taskManager.addTodo(command.getArgument(0)));
                return false;
            case DEADLINE:
                announceAdded(taskManager.addDeadline(command.getArgument(0), command.getArgument(1)));
                return false;
            case EVENT:
                announceAdded(taskManager.addEvent(command.getArgument(0), command.getArgument(1),
                        command.getArgument(2)));
                return false;
            case MARK:
                announceStatus(taskManager.markTask(Integer.parseInt(command.getArgument(0))), true);
                return false;
            case UNMARK:
                announceStatus(taskManager.unmarkTask(Integer.parseInt(command.getArgument(0))), false);
                return false;
            case BYE:
                handleBye();
                return true;
            default:
                throw new EchoException("I could not process that command. Try 'help' to see available commands.");
        }
    }

    /** Displays the commands and formats supported by E.C.H.O. */
    private void handleHelp() {
        printBotResponse(HELP_MESSAGE);
    }

    /** Displays every task in the order in which it was added. */
    private void handleList() {
        if (taskManager.isEmpty()) {
            printBotResponse("List is empty!");
            return;
        }

        StringBuilder response = new StringBuilder("Your tasks:\n");
        for (int i = 1; i <= taskManager.size(); i++) {
            response.append(i)
                    .append(": ")
                    .append(taskManager.getTask(i))
                    .append("\n");
        }
        printBotResponse(response.toString().stripTrailing());
    }

    /** Prints a random farewell before ending the session. */
    private void handleBye() {
        String farewell = FAREWELL_FLAVORS.get(RANDOM.nextInt(FAREWELL_FLAVORS.size()));
        printBotResponse(farewell);
    }

    /** Displays the task created by a successful add command. */
    private void announceAdded(Task task) {
        printBotResponse("Added the following task:\n  " + task
                + "\nTotal tasks: " + taskManager.size());
    }

    /** Displays the result of marking or unmarking a task. */
    private void announceStatus(Task task, boolean marked) {
        String action = marked ? "marked" : "unmarked";
        printBotResponse("Task " + action + " successfully:\n  " + task);
    }

    /** Prints a message using the standard E.C.H.O. response layout. */
    private void printBotResponse(String message) {
        System.out.println(SEPARATOR);
        System.out.println(wrapResponse(message));
        System.out.println(SEPARATOR + "\n");
    }

    /** Wraps each response line so it fits within the separator width. */
    private String wrapResponse(String message) {
        String[] lines = message.split("\\R", -1);
        StringBuilder wrapped = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                wrapped.append("\n");
            }
            appendWrappedLine(wrapped, lines[i]);
        }

        return wrapped.toString();
    }

    /** Appends one response line, breaking it at a word boundary when possible. */
    private void appendWrappedLine(StringBuilder output, String line) {
        if (line.isEmpty()) {
            output.append(RESPONSE_INDENT);
            return;
        }

        String remaining = line;
        boolean firstSegment = true;

        while (!remaining.isEmpty()) {
            if (!firstSegment) {
                output.append("\n");
            }

            int end = Math.min(RESPONSE_CONTENT_WIDTH, remaining.length());
            int breakAt = end < remaining.length() ? findWordBreak(remaining, end) : end;

            output.append(RESPONSE_INDENT).append(remaining, 0, breakAt);
            remaining = remaining.substring(breakAt).stripLeading();
            firstSegment = false;
        }
    }

    /** Finds the last whitespace before the width limit, or uses a hard break. */
    private int findWordBreak(String line, int end) {
        int firstContent = 0;
        while (firstContent < line.length() && Character.isWhitespace(line.charAt(firstContent))) {
            firstContent++;
        }

        for (int i = end - 1; i > firstContent; i--) {
            if (Character.isWhitespace(line.charAt(i))) {
                return i;
            }
        }

        return end;
    }
}
