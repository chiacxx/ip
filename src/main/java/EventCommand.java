import java.util.List;

/**
 * Creates an event task.
 */
public class EventCommand extends TaskCreationCommand {
    /**
     * Creates an event command.
     *
     * @param description Event description.
     * @param startDate Validated event start date.
     * @param endDate Validated event end date.
     */
    public EventCommand(String description, String startDate, String endDate) {
        super(Type.EVENT, List.of(description, startDate, endDate));
    }

    /** Creates and displays an event task. */
    @Override
    public void execute(TaskManager taskManager, Ui ui) {
        showAdded(taskManager.addEvent(getDescription(), getArgument(1), getArgument(2)),
                taskManager, ui);
    }
}
