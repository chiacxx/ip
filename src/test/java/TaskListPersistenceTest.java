import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Basic read/write checks for task-list persistence. */
public class TaskListPersistenceTest {
    /** Runs the persistence checks without requiring an external test framework. */
    public static void main(String[] args) throws Exception {
        Path temporaryDirectory = Files.createTempDirectory("echo-task-list-test");
        Path temporaryFile = temporaryDirectory.resolve("echo.txt");

        try {
            TaskList taskList = new TaskList(temporaryFile);
            TaskManager taskManager = new TaskManager(taskList);

            taskManager.addTodo("read book");
            taskManager.addDeadline("return book", "June 6th");
            taskManager.addEvent("project meeting", "Aug 6th", "2-4pm");
            assertFileContents(temporaryFile, List.of(
                    "T | 0 | read book",
                    "D | 0 | return book | June 6th",
                    "E | 0 | project meeting | Aug 6th 2-4pm"));

            taskManager.markTask(1);
            assertFileContents(temporaryFile, List.of(
                    "T | 1 | read book",
                    "D | 0 | return book | June 6th",
                    "E | 0 | project meeting | Aug 6th 2-4pm"));

            taskManager.unmarkTask(1);
            taskManager.deleteTask(2);
            assertFileContents(temporaryFile, List.of(
                    "T | 0 | read book",
                    "E | 0 | project meeting | Aug 6th 2-4pm"));

            TaskList loadedTaskList = new TaskList(temporaryFile);
            if (!loadedTaskList.getTask(1).toString().contains("read book")
                    || !loadedTaskList.getTask(2).toString().contains("project meeting")) {
                throw new AssertionError("Saved tasks were not loaded correctly");
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
            Files.deleteIfExists(temporaryDirectory);
        }
    }

    /** Checks the exact lines written by the storage implementation. */
    private static void assertFileContents(Path file, List<String> expected) throws Exception {
        List<String> actual = Files.readAllLines(file);
        if (!actual.equals(expected)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }
}
