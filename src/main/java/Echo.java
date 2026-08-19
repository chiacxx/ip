import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * E.C.H.O. - Everyday Conversational & Helpful Operator.
 */
public class Echo {
    private static final String SEPARATOR = "────────────────────────────────────────────────────────────────";
    private static final String PROMPT = "echo ❯ ";
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
    private static int messageCount = 0;
    private static final Instant SESSION_START = Instant.now();

    private static final String[] messages = new String[100];

    public static void main(String[] args) {
        System.out.println(BANNER);

        printBotResponse("Signal established. Online and listening!\nType 'bye' to disconnect.");

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

                switch (input.toLowerCase(Locale.ROOT)) {
                    case "list":
                        String response = "";
                        for (int i = 0; i < messageCount; i++) {
                            response += i + 1 + ": " + messages[i] + "\n";
                        }
                        printBotResponse(response);
                        break;

                    case "bye":
                        String farewell = FAREWELL_FLAVORS.get(RANDOM.nextInt(FAREWELL_FLAVORS.size()));
                        printBotResponse(farewell + "\n" + sessionSummary());
                        return;

                    default:
                        messages[messageCount++] = input;
                        printBotResponse("Added: " + input);
                        break;
                }
            }
        }
    }

    private static String sessionSummary() {
        Duration uptime = Duration.between(SESSION_START, Instant.now());
        return "Session stats -> messages: " + messageCount
                + ", uptime: " + formatDuration(uptime);
    }

    private static String formatDuration(Duration d) {
        long minutes = d.toMinutes();
        long seconds = d.minusMinutes(minutes).getSeconds();
        return minutes + "m " + seconds + "s";
    }

    /**
     * Formats and prints a multi-line message wrapped inside clean divider rails.
     *
     * @param message Message to display to the user.
     */
    private static void printBotResponse(String message) {
        System.out.println(SEPARATOR);

        // Indents multi-line output cleanly under the ECHO tag
        String formatted = message.replace("\n", "\n" + " ".repeat(PREFIX.length()));
        System.out.println(PREFIX + formatted);

        System.out.println(SEPARATOR + "\n");
    }
}
