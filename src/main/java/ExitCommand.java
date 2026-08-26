import java.util.List;

/**
 * Ends the current E.C.H.O. session.
 */
public class ExitCommand extends Command {
    /** Creates an exit command without arguments. */
    public ExitCommand() {
        super(Type.BYE, List.of());
    }

    /** Displays a farewell message before the session ends. */
    @Override
    public void execute(TaskManager taskManager, Ui ui) {
        ui.showFarewell();
    }

    /** An exit command ends the session after execution. */
    @Override
    public boolean isExit() {
        return true;
    }
}
