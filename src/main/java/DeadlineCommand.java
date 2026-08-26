import java.util.List;

/**
 * Creates a deadline task.
 */
public class DeadlineCommand extends TaskCreationCommand {
    /**
     * Creates a deadline command.
     *
     * @param description Deadline description.
     * @param dueDate Validated deadline date.
     */
    public DeadlineCommand(String description, String dueDate) {
        super(Type.DEADLINE, List.of(description, dueDate));
    }

    /** Creates and displays a deadline task. */
    @Override
    public void execute(TaskManager taskManager, Ui ui) {
        showAdded(taskManager.addDeadline(getDescription(), getArgument(1)), taskManager, ui);
    }
}
