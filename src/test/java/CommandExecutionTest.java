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
        if (!(new CommandParser().parse("mark 1") instanceof MarkCommand)) {
            throw new AssertionError("mark should create a MarkCommand");
        }
        if (!(new CommandParser().parse("unmark 1") instanceof UnmarkCommand)) {
            throw new AssertionError("unmark should create an UnmarkCommand");
        }
        if (!(new CommandParser().parse("delete 1") instanceof DeleteCommand)) {
            throw new AssertionError("delete should create a DeleteCommand");
        }
        if (!(new CommandParser().parse("todo read book") instanceof TodoCommand)) {
            throw new AssertionError("todo should create a TodoCommand");
        }
        if (!(new CommandParser().parse("deadline return book /by 15-10-2019")
                instanceof DeadlineCommand)) {
            throw new AssertionError("deadline should create a DeadlineCommand");
        }
        if (!(new CommandParser().parse("event meeting /from 15-10-2019 /to 16-10-2019")
                instanceof EventCommand)) {
            throw new AssertionError("event should create an EventCommand");
        }
    }
}
