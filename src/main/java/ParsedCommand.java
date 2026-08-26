/**
 * Executes the existing parsed command representation during the command-class migration.
 *
 * <p>This temporary implementation keeps all current command behavior in one place while
 * individual command classes are extracted in later increments.</p>
 */
public class ParsedCommand extends Command {
    /**
     * Creates a parsed command with already-validated arguments.
     *
     * @param type Command type.
     * @param arguments Validated command arguments.
     */
    public ParsedCommand(Type type, java.util.List<String> arguments) {
        super(type, arguments);
    }

    /** Executes the parsed command using the existing command behavior. */
    @Override
    public void execute(TaskManager taskManager, Ui ui) throws EchoException {
        switch (getType()) {
        case TODO:
            ui.showAdded(taskManager.addTodo(getArgument(0)), taskManager.size());
            return;
        case DEADLINE:
            ui.showAdded(taskManager.addDeadline(getArgument(0), getArgument(1)), taskManager.size());
            return;
        case EVENT:
            ui.showAdded(taskManager.addEvent(getArgument(0), getArgument(1), getArgument(2)),
                    taskManager.size());
            return;
        default:
            throw new EchoException("I could not process that command. Try 'help' to see available commands.");
        }
    }
}
