/**
 * Marks a task as done.
 */
public class MarkCommand extends TaskNumberCommand {
    /**
     * Creates a mark command.
     *
     * @param taskNumber One-based task number.
     */
    public MarkCommand(int taskNumber) {
        super(Type.MARK, taskNumber);
    }

    /** Marks the selected task and displays the result. */
    @Override
    public void execute(TaskManager taskManager, Ui ui) throws EchoException {
        ui.showStatus(taskManager.markTask(getTaskNumber()), true);
    }
}
