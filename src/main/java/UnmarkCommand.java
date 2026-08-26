/**
 * Marks a task as not done.
 */
public class UnmarkCommand extends TaskNumberCommand {
    /**
     * Creates an unmark command.
     *
     * @param taskNumber One-based task number.
     */
    public UnmarkCommand(int taskNumber) {
        super(Type.UNMARK, taskNumber);
    }

    /** Unmarks the selected task and displays the result. */
    @Override
    public void execute(TaskManager taskManager, Ui ui) throws EchoException {
        ui.showStatus(taskManager.unmarkTask(getTaskNumber()), false);
    }
}
