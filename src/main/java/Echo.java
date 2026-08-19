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

    private static int taskCount = 0;
    private static final Task[] tasks = new Task[100];

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
                        StringBuilder response = new StringBuilder("Your tasks:\n");
                        for (int i = 0; i < taskCount; i++) {
                            response.append(i + 1)
                                    .append(": ")
                                    .append(tasks[i])
                                    .append("\n");
                        }
                        printBotResponse(response.toString().stripTrailing());
                        break;

                    case "mark":
                        updateTaskStatus(commandParts, true);
                        break;

                    case "unmark":
                        updateTaskStatus(commandParts, false);
                        break;

                    case "bye":
                        String farewell = FAREWELL_FLAVORS.get(RANDOM.nextInt(FAREWELL_FLAVORS.size()));
                        printBotResponse(farewell);
                        return;

                    default:
                        tasks[taskCount++] = new Task(input);
                        printBotResponse("Added: " + input);
                        break;
                }
            }
        }
    }

    /**
     * Marks or unmarks the task identified by a one-based command-line index.
     *
     * @param commandParts the command and its arguments
     * @param done whether the task should be marked done
     */
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

        if (taskNumber < 1 || taskNumber > taskCount) {
            printBotResponse("There is no task with that number.");
            return;
        }

        Task task = tasks[taskNumber - 1];

        if (done) {
            task.mark();
            printBotResponse("Task marked successfully:\n  " + task);
        } else {
            task.unmark();
            printBotResponse("Task unmarked successfully:\n  " + task);
        }
    }

    /**
     * Formats and prints a multi-line message wrapped inside clean divider rails.
     *
     * @param message Message to display to the user.
     */
    private static void printBotResponse(String message) {
        System.out.println(SEPARATOR);

        String formatted = message.replace("\n", "\n" + " ".repeat(PREFIX.length()));
        System.out.println(PREFIX + formatted);

        System.out.println(SEPARATOR + "\n");
    }
}
