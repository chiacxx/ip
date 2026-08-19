import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * E.C.H.O. - Everyday Conversational & Helpful Operator.
 */
public class Echo {
    private static final String SEPARATOR = "────────────────────────────────────────────────────────────────";
    private static final String PREFIX = "  [ECHO] ";

    private static final String BANNER = """
       ______ _____ _   _  ____  \s
      |  ____/ ____| | | |/ __ \\ \s
      | |__ | |    | |_| | |  | |
      |  __|| |    |  _  | |  | |
      | |___| |____| | | | |__| |
      |______\\_____|_| |_|\\____/ \s
      + ──────────────────────────────────────────────────────────── +
      |  [E]veryday [C]onversational & [H]elpful [O]perator
      + ──────────────────────────────────────────────────────────── +""";

    private static final List<String> FAREWELL_FLAVORS = List.of(
            "Signal fading... E.C.H.O. signing off. Take care!",
            "Powering down the transmitter. Catch you soon!",
            "Going dark now. Thanks for the chat!"
    );

    private static final Random RANDOM = new Random();

    /** Stores tasks independently from the command-line user interface. */
    private static final TaskList taskList = new TaskList();

    public static void main(String[] args) {
        System.out.println(BANNER);

        printBotResponse("Signal established. Online and listening!\nType 'bye' to disconnect.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("echo ❯ ");

                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    continue;
                }

                String[] commandParts = input.split("\\s+");
                String command = commandParts[0].toLowerCase(Locale.ROOT);

                switch (command) {
                    case "list":
                        handleList();
                        break;

                    case "todo":
                        handleTodo(input);
                        break;

                    case "deadline":
                        handleDeadline(input);
                        break;

                    case "event":
                        handleEvent(input);
                        break;

                    case "mark":
                        updateTaskStatus(commandParts, true);
                        break;

                    case "unmark":
                        updateTaskStatus(commandParts, false);
                        break;

                    case "bye":
                        handleBye();
                        return;

                    default:
                        printBotResponse("Invalid command!");
                        break;
                }
            }
        }
    }

    /** Displays every task in the order in which it was added. */
    private static void handleList() {
        if (taskList.isEmpty()) {
            printBotResponse("List is empty!");
            return;
        }

        StringBuilder response = new StringBuilder("Your tasks:\n");
        for (int i = 1; i <= taskList.size(); i++) {
            response.append(i)
                    .append(": ")
                    .append(taskList.getTask(i))
                    .append("\n");
        }
        printBotResponse(response.toString().stripTrailing());
    }

    /** Prints a random farewell before ending the session. */
    private static void handleBye() {
        String farewell = FAREWELL_FLAVORS.get(RANDOM.nextInt(FAREWELL_FLAVORS.size()));
        printBotResponse(farewell);
    }

    /** Creates and stores a todo task from the text following the command. */
    private static void handleTodo(String input) {
        String description = getCommandContent(input);

        if (description.isEmpty()) {
            printBotResponse("The description of a todo cannot be empty!");
            return;
        }

        addTask(new ToDo(description));
    }

    /** Creates and stores a deadline task after validating its {@code /by} field. */
    private static void handleDeadline(String input) {
        String content = getCommandContent(input);
        String[] parts = content.split("(?i)\\s+/by\\s+", 2);

        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            printBotResponse("Please specify a description and a deadline using '/by <deadline>'");
            return;
        }

        addTask(new Deadline(parts[0].trim(), parts[1].trim()));
    }

    /** Creates and stores an event task after validating its {@code /from} and {@code /to} fields. */
    private static void handleEvent(String input) {
        String content = getCommandContent(input);
        String[] partsFrom = content.split("(?i)\\s+/from\\s+", 2);

        if (partsFrom.length < 2 || partsFrom[0].isBlank()) {
            printBotResponse("Please use format: event <desc> /from <start> /to <end>");
            return;
        }

        String description = partsFrom[0].trim();
        String[] partsTo = partsFrom[1].split("(?i)\\s+/to\\s+", 2);

        if (partsTo.length < 2 || partsTo[0].isBlank() || partsTo[1].isBlank()) {
            printBotResponse("Please use format: event <desc> /from <start> /to <end>");
            return;
        }

        addTask(new Event(description, partsTo[0].trim(), partsTo[1].trim()));
    }

    /**
     * Extracts the portion of an input line after its first whitespace-separated command.
     *
     * @param input complete input line from the user
     * @return command arguments, or an empty string when no arguments were supplied
     */
    private static String getCommandContent(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (Character.isWhitespace(input.charAt(i))) {
                return input.substring(i + 1).trim();
            }
        }
        return "";
    }

    /** Adds a task and reports the resulting task to the user. */
    private static void addTask(Task task) {
        taskList.addTask(task);
        printBotResponse("Added the following task:\n  " + task + "\nTotal tasks: " + taskList.size());
    }

    /** Marks or unmarks a task using the one-based number shown by {@code list}. */
    private static void updateTaskStatus(String[] commandParts, boolean done) {
        if (commandParts.length != 2) {
            printBotResponse("Please provide a task number.");
            return;
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(commandParts[1]);
        } catch (NumberFormatException exception) {
            printBotResponse("Please provide a valid task number.");
            return;
        }

        if (!taskList.hasTask(taskNumber)) {
            printBotResponse("There is no task with that number.");
            return;
        }

        Task task = taskList.getTask(taskNumber);

        if (done) {
            task.mark();
            printBotResponse("Task marked successfully:\n  " + task);
        } else {
            task.unmark();
            printBotResponse("Task unmarked successfully:\n  " + task);
        }
    }

    /** Prints a message using the standard E.C.H.O. response layout. */
    private static void printBotResponse(String message) {
        System.out.println(SEPARATOR);
        String formatted = message.replace("\n", "\n" + " ".repeat(PREFIX.length()));
        System.out.println(PREFIX + formatted);
        System.out.println(SEPARATOR + "\n");
    }
}
