/**
 * Provides the command-line interface for E.C.H.O., the Everyday Conversational
 * and Helpful Operator.
 */
public class Echo {
    /** User interface for input and output. */
    private final Ui ui;
    /** Parser for converting user input into commands. */
    private final CommandParser commandParser;
    /** Manager for task operations and persistence. */
    private final TaskManager taskManager;

    /**
     * Creates a new E.C.H.O. session with tasks loaded from local storage.
     */
    public Echo() {
        this.ui = new Ui();
        this.commandParser = new CommandParser();
        this.taskManager = new TaskManager();
    }

    /**
     * Starts an E.C.H.O. session.
     *
     * @param args Command-line arguments, which are currently unused.
     */
    public static void main(String[] args) {
        new Echo().run();
    }

    /** Reads and processes input until the user disconnects or input ends. */
    private void run() {
        ui.showWelcome();

        try {
            while (true) {
                String input = ui.readCommand();
                if (input == null) {
                    break;
                }

                if (input.isEmpty()) {
                    continue;
                }

                try {
                    if (execute(commandParser.parse(input))) {
                        break;
                    }
                } catch (EchoException exception) {
                    ui.showError(exception.getMessage());
                }
            }
        } finally {
            ui.close();
        }
    }

    /**
     * Executes a validated command.
     *
     * @param command Validated command.
     * @return Whether the session should end.
     * @throws EchoException If the command refers to a task that does not exist.
     */
    private boolean execute(Command command) throws EchoException {
        switch (command.getType()) {
            case HELP:
                ui.showHelp();
                return false;
            case LIST:
                ui.showTaskList(taskManager.getTasks());
                return false;
            case TODO:
                ui.showAdded(taskManager.addTodo(command.getArgument(0)), taskManager.size());
                return false;
            case DEADLINE:
                ui.showAdded(taskManager.addDeadline(command.getArgument(0), command.getArgument(1)),
                        taskManager.size());
                return false;
            case EVENT:
                ui.showAdded(taskManager.addEvent(command.getArgument(0), command.getArgument(1),
                        command.getArgument(2)), taskManager.size());
                return false;
            case MARK:
                ui.showStatus(taskManager.markTask(Integer.parseInt(command.getArgument(0))), true);
                return false;
            case UNMARK:
                ui.showStatus(taskManager.unmarkTask(Integer.parseInt(command.getArgument(0))), false);
                return false;
            case DELETE:
                int taskNumber = Integer.parseInt(command.getArgument(0));
                ui.showDeleted(taskNumber, taskManager.deleteTask(taskNumber), taskManager.size());
                return false;
            case BYE:
                ui.showFarewell();
                return true;
            default:
                throw new EchoException("I could not process that command. Try 'help' to see available commands.");
        }
    }

}
