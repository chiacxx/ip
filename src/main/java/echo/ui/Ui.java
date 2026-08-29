package echo.ui;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import echo.task.Task;

/**
 * Handles all command-line interaction for E.C.H.O.
 *
 * <p>This class owns input reading and response formatting. It does not parse
 * commands or modify tasks.</p>
 */
public class Ui implements AutoCloseable {
    /** Horizontal line used to frame E.C.H.O. responses. */
    private static final String SEPARATOR = "-----------------------------------------------------------------------------------";

    /** Prompt displayed while waiting for user input. */
    private static final String PROMPT = "E.C.H.O. >> ";
    /** Indentation applied to each response line. */
    private static final String RESPONSE_INDENT = "  ";
    /** Maximum response content width before a line is wrapped. */
    private static final int RESPONSE_CONTENT_WIDTH = SEPARATOR.length() - RESPONSE_INDENT.length();

    /** Help text describing the commands supported by E.C.H.O. */
    private static final String HELP_MESSAGE = """
            Available operations:
              help                                         Show this help message
              list                                         Display all tasks
              todo <description>                           Add a todo task
              deadline <description> /by <dd-mm-yyyy> [HH:MM]  Add a deadline task
              event <description> /from <dd-mm-yyyy> [HH:MM] /to <dd-mm-yyyy> [HH:MM]  Add an event task
              mark <number>                                Mark a task as done
              unmark <number>                              Mark a task as not done
              delete <number>                              Remove a task
              find <keyword>                               Find tasks by keyword
              bye                                          Disconnect from E.C.H.O.

            Task numbers are shown by the 'list' command.""";

    /** Startup banner displayed when an E.C.H.O. session begins. */
    private static final String BANNER = """
       ______ _____ _   _  ____  \s
      |  ____/ ____| | | |/ __ \\ \s
      | |__ | |    | |_| | |  | |
      |  __|| |    |  _  | |  | |
      | |___| |____| | | | |__| |
      |______\\_____|_| |_|\\____/ \s
      """;

    /** Farewell messages selected randomly when a session ends. */
    private static final List<String> FAREWELL_FLAVORS = List.of(
            "Signal fading... E.C.H.O. signing off. Take care!",
            "Powering down the transmitter. Catch you soon!",
            "Going dark now. Thanks for the chat!"
    );

    /** Random generator used to select a farewell message. */
    private static final Random RANDOM = new Random();

    /** Input source used by this interface. */
    private final Scanner scanner;

    /** Creates an interface that reads from standard input. */
    public Ui() {
        this(new Scanner(System.in));
    }

    /**
     * Creates an interface using the supplied input source.
     *
     * @param scanner Input source used to read commands.
     */
    public Ui(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Displays the startup banner and connection message. */
    public void showWelcome() {
        System.out.println(BANNER);
        showMessage("Signal established. Online and listening!\nType 'help' to view list of operations!");
    }

    /**
     * Reads one trimmed command from the user.
     *
     * @return The command, or {@code null} when input has ended.
     */
    public String readCommand() {
        System.out.print(PROMPT);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    /** Displays the available commands. */
    public void showHelp() {
        showMessage(HELP_MESSAGE);
    }

    /** Displays an error message. */
    public void showError(String message) {
        showMessage(message);
    }

    /** Displays a recoverable error that occurred while loading saved tasks. */
    public void showLoadingError() {
        showError("I could not load your saved tasks. Starting with an empty list.");
    }

    /** Displays all tasks, or an empty-list message when there are no tasks. */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            showMessage("List is empty!");
            return;
        }

        StringBuilder response = new StringBuilder("Your tasks:\n");
        for (int i = 0; i < tasks.size(); i++) {
            response.append(i + 1)
                    .append(": ")
                    .append(tasks.get(i))
                    .append("\n");
        }
        showMessage(response.toString().stripTrailing());
    }

    /** Displays a randomly selected farewell message. */
    public void showFarewell() {
        String farewell = FAREWELL_FLAVORS.get(RANDOM.nextInt(FAREWELL_FLAVORS.size()));
        showMessage(farewell);
    }

    /** Displays the task created by a successful add command. */
    public void showAdded(Task task, int totalTasks) {
        showMessage("Added the following task:\n  " + task
                + "\nTotal tasks: " + totalTasks);
    }

    /** Displays the result of marking or unmarking a task. */
    public void showStatus(Task task, boolean marked) {
        String action = marked ? "marked" : "unmarked";
        showMessage("Task " + action + " successfully:\n  " + task);
    }

    /** Displays the removed task and the number of tasks remaining. */
    public void showDeleted(int taskNumber, Task task, int remainingTasks) {
        showMessage("Successfully removed Task #" + taskNumber + ":\n  " + task
                + "\nTotal tasks: " + remainingTasks);
    }

    /** Releases the input source owned by this interface. */
    @Override
    public void close() {
        scanner.close();
    }

    /** Prints a message using the standard E.C.H.O. response layout. */
    private void showMessage(String message) {
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
