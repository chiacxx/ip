import java.util.List;

/** Checks date validation for deadline and event commands. */
public class CommandParserDateTest {
    /** Runs the date validation checks without requiring an external test framework. */
    public static void main(String[] args) throws Exception {
        CommandParser parser = new CommandParser();

        assertArguments(parser.parse("deadline submit report /by 06-06-2026"),
                Command.Type.DEADLINE, List.of("submit report", "06-06-2026"));
        assertArguments(parser.parse("event project meeting /from 06-06-2026 /to 07-06-2026"),
                Command.Type.EVENT, List.of("project meeting", "06-06-2026", "07-06-2026"));

        assertRejected(parser, "deadline submit report /by 6-6-2026");
        assertRejected(parser, "deadline submit report /by 31-02-2026");
        assertRejected(parser, "event project meeting /from 06/06/2026 /to 07-06-2026");
        assertRejected(parser, "event project meeting /from 06-06-2026 /to 31-02-2026");
    }

    /** Checks the command type and arguments returned by the parser. */
    private static void assertArguments(Command command, Command.Type expectedType,
                                        List<String> expectedArguments) {
        List<String> actualArguments = command.getType() == Command.Type.EVENT
                ? List.of(command.getArgument(0), command.getArgument(1), command.getArgument(2))
                : List.of(command.getArgument(0), command.getArgument(1));
        if (command.getType() != expectedType || !expectedArguments.equals(actualArguments)) {
            throw new AssertionError("Unexpected parsed command");
        }
    }

    /** Checks that an invalid date input is rejected with a format-related message. */
    private static void assertRejected(CommandParser parser, String input) throws Exception {
        try {
            parser.parse(input);
            throw new AssertionError("Expected input to be rejected: " + input);
        } catch (EchoException exception) {
            if (!exception.getMessage().contains("dd-mm-yyyy")) {
                throw new AssertionError("Date error did not mention dd-mm-yyyy: "
                        + exception.getMessage());
            }
        }
    }
}
