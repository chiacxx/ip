package echo;

import java.io.UncheckedIOException;
import java.nio.file.Paths;

import echo.command.Command;
import echo.command.CommandParser;
import echo.storage.Storage;
import echo.task.TaskList;
import echo.task.TaskManager;
import echo.ui.Ui;

/**
 * Provides the command-line interface for E.C.H.O., the Everyday Conversational
 * and Helpful Operator.
 */
public class Echo {
    /** User interface for input and output. */
    private final Ui ui;
    /** Storage used to load and save the application's tasks. */
    private final Storage storage;
    /** Parser for converting user input into commands. */
    private final CommandParser commandParser;
    /** Manager for task operations and persistence. */
    private final TaskManager taskManager;

    /** Creates a new E.C.H.O. session using the default storage file. */
    public Echo() {
        this(new Storage());
    }

    /**
     * Creates a new E.C.H.O. session using a specific storage file.
     *
     * @param filePath File used to load and save tasks.
     */
    public Echo(String filePath) {
        this(new Storage(Paths.get(filePath)));
    }

    /**
     * Creates a new E.C.H.O. session using the supplied storage.
     *
     * @param storage Storage used to load and save tasks.
     */
    public Echo(Storage storage) {
        this(new Ui(), storage);
    }

    /**
     * Creates a session with explicit UI and storage dependencies.
     *
     * @param ui User interface used by the session.
     * @param storage Storage used to load and save tasks.
     */
    public Echo(Ui ui, Storage storage) {
        this.ui = ui;
        this.storage = storage;
        this.commandParser = new CommandParser();

        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (UncheckedIOException exception) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
        this.taskManager = new TaskManager(tasks, this.storage);
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
    public void run() {
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
                    Command command = commandParser.parse(input);
                    command.execute(taskManager, ui);
                    if (command.isExit()) {
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

}
