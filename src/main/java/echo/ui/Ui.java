package echo.ui;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import echo.task.Task;

/**
 * Handles all command-line interaction for E.C.H.O.
 */
public class Ui implements AutoCloseable {
    /** Horizontal line used to frame E.C.H.O. responses. */

    private static final String SEPARATOR = "----------------------------------------"
            + "-------------------------------------------";

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
    private static final List<String> FAREWELL_FLAVOURS = List.of(
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
     * @param scanner input source used to read commands.
     */
    public Ui(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Displays the startup banner and connection message.
     *
     * @return the welcome message.
     */
    public String showWelcome() {
        System.out.println(BANNER);
        return showMessage(getWelcomeMessage());
    }

    /**
     * Returns the startup welcome message.
     *
     * @return the welcome message.
     */
    public String getWelcomeMessage() {
        return "Signal established. Online and listening!\nType 'help' to view list of operations!";
    }

    /**
     * Reads one trimmed command from the user.
     *
     * @return the command, or {@code null} when input has ended.
     */
    public String readCommand() {
        System.out.print(PROMPT);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    /**
     * Displays the available commands.
     *
     * @return the help message.
     */
    public String showHelp() {
        return showMessage(HELP_MESSAGE);
    }

    /**
     * Displays an error message.
     *
     * @param message error message to be displayed.
     * @return the error message.
     */
    public String showError(String message) {
        return showMessage(message);
    }

    /**
     * Displays a recoverable error that occurred while loading saved tasks.
     *
     * @return the loading error message.
     */
    public String showLoadingError() {
        return showError("I could not load your saved tasks. Starting with an empty list.");
    }

    /**
     * Displays all tasks, or an empty-list message when there are no tasks.
     *
     * @param tasks list of tasks.
     * @return the task list message.
     */
    public String showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return showMessage("List is empty!");
        }

        String taskList = IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + ": " + tasks.get(i))
                .collect(Collectors.joining("\n"));
        return showMessage("Your tasks:\n" + taskList);
    }

    /**
     * Displays a randomly selected farewell message.
     *
     * @return the farewell message.
     */
    public String showFarewell() {
        assert !FAREWELL_FLAVOURS.isEmpty() : "At least one farewell message must be configured";
        String farewell = FAREWELL_FLAVOURS.get(RANDOM.nextInt(FAREWELL_FLAVOURS.size()));
        return showMessage(farewell);
    }

    /**
     * Displays the task created by a successful add command.
     *
     * @param task the new task created.
     * @param totalTasks total number of tasks.
     * @return the task added message.
     */
    public String showAdded(Task task, int totalTasks) {
        return showMessage("Added the following task:\n  " + task
                + "\nTotal tasks: " + totalTasks);
    }

    /**
     * Displays the result of marking or unmarking a task.
     *
     * @param task Task whose status changed.
     * @param isMarked Whether the task is now marked as done.
     * @return the status change message.
     */
    public String showStatus(Task task, boolean isMarked) {
        String action = isMarked ? "marked" : "unmarked";
        return showMessage("Task " + action + " successfully:\n  " + task);
    }

    /**
     * Displays the removed task and the number of tasks remaining.
     *
     * @param taskNumber task number of the removed task.
     * @param task the task that has been removed.
     * @param remainingTasks remaning number of tasks.
     * @return the task deleted message.
     */
    public String showDeleted(int taskNumber, Task task, int remainingTasks) {
        return showMessage("Successfully removed Task #" + taskNumber + ":\n  " + task
                + "\nTotal tasks: " + remainingTasks);
    }

    /** Releases the input source owned by this interface. */
    @Override
    public void close() {
        scanner.close();
    }

    /** Prints a message using the standard E.C.H.O. response layout. */
    private String showMessage(String message) {
        System.out.println(SEPARATOR);
        System.out.println(wrapResponse(message));
        System.out.println(SEPARATOR + "\n");
        return message;
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
        boolean isFirstSegment = true;

        while (!remaining.isEmpty()) {
            if (!isFirstSegment) {
                output.append("\n");
            }

            int end = Math.min(RESPONSE_CONTENT_WIDTH, remaining.length());
            int breakAt = end < remaining.length() ? findWordBreak(remaining, end) : end;
            assert breakAt > 0 && breakAt <= remaining.length()
                    : "Response wrapping must consume at least one character";

            output.append(RESPONSE_INDENT).append(remaining, 0, breakAt);
            remaining = remaining.substring(breakAt).stripLeading();
            isFirstSegment = false;
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
