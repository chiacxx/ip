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
    public String execute(TaskManager taskManager, Ui ui) {
        return showAdded(taskManager.addTodo(getDescription()), taskManager, ui);
    }
}
