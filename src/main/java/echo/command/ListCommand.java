package echo.command;

import echo.task.TaskManager;
import echo.ui.Ui;

/**
 * Displays the user's tasks.
 */
public class ListCommand extends Command {
    /** Creates a list command without arguments. */
    public ListCommand() {
        super(Type.LIST, java.util.List.of());
    }

    /** Displays all tasks managed by E.C.H.O. */
    @Override
    public void execute(TaskManager taskManager, Ui ui) {
        ui.showTaskList(taskManager.getTasks());
    }
}
