package echo.command;

import java.util.List;

import echo.task.TaskManager;
import echo.ui.Ui;

/**
 * Represents a todo task command.
 */
public class TodoCommand extends TaskCreationCommand {
    /**
     * Creates a todo command.
     *
     * @param description todo description.
     */
    public TodoCommand(String description) {
        super(Type.TODO, List.of(description));
    }

    /**
     * Creates and displays a todo task.
     */
    @Override
    public void execute(TaskManager taskManager, Ui ui) {
        showAdded(taskManager.addTodo(getDescription()), taskManager, ui);
    }
}
