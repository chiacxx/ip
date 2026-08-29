package echo.command;

import java.util.List;

import echo.task.Task;
import echo.task.TaskManager;
import echo.ui.Ui;

/**
 * Base class for commands that create tasks from a description.
 */
public abstract class TaskCreationCommand extends Command {
    /**
     * Creates a task-creation command.
     *
     * @param type command type.
     * @param arguments validated task-creation command arguments.
     */
    protected TaskCreationCommand(Type type, List<String> arguments) {
        super(type, arguments);
    }

    /**
     * Returns the task description.
     */
    protected String getDescription() {
        return getArgument(0);
    }

    /**
     * Displays a newly created task using the supplied UI service.
     */
    protected void showAdded(Task task, TaskManager taskManager, Ui ui) {
        ui.showAdded(task, taskManager.size());
    }
}
