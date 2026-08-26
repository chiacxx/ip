package echo.command;

import echo.task.TaskManager;
import echo.ui.Ui;

/**
 * Displays E.C.H.O.'s command guidance.
 */
public class HelpCommand extends Command {
    /** Creates a help command without arguments. */
    public HelpCommand() {
        super(Type.HELP, java.util.List.of());
    }

    /** Displays the supported commands. */
    @Override
    public void execute(TaskManager taskManager, Ui ui) {
        ui.showHelp();
    }
}
