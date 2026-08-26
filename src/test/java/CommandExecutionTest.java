/** Checks that the exit command is represented by its dedicated command class. */
public class CommandExecutionTest {
    /** Runs the exit-command checks without requiring an external test framework. */
    public static void main(String[] args) throws Exception {
        Command command = new CommandParser().parse("bye");
        if (!(command instanceof ExitCommand) || !command.isExit()) {
            throw new AssertionError("bye should create an exiting ExitCommand");
        }

        try {
            new CommandParser().parse("bye now");
            throw new AssertionError("bye should reject arguments");
        } catch (EchoException exception) {
            if (!exception.getMessage().contains("does not take any arguments")) {
                throw new AssertionError("Unexpected bye error: " + exception.getMessage());
            }
        }

        if (!(new CommandParser().parse("help") instanceof HelpCommand)) {
            throw new AssertionError("help should create a HelpCommand");
        }
        if (!(new CommandParser().parse("list") instanceof ListCommand)) {
            throw new AssertionError("list should create a ListCommand");
        }
    }
}
