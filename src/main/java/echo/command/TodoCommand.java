package echo.command;

import java.util.List;

import echo.task.TaskManager;
import echo.ui.Ui;

/**
 * Creates a todo task.
 */
public class TodoCommand extends TaskCreationCommand {
    /**
     * Creates a todo command.
     *
     * @param description Todo description.
     */
    public TodoCommand(String description) {
        super(Type.TODO, List.of(description));
    }

    /** Creates and displays a todo task. */
    @Override
    public void execute(TaskManager taskManager, Ui ui) {
        showAdded(taskManager.addTodo(getDescription()), taskManager, ui);
    }
}
