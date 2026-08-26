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
            Storage storage = new Storage(temporaryFile);
            TaskList taskList = new TaskList(storage.load());
            TaskManager taskManager = new TaskManager(taskList, storage);

            taskManager.addTodo("read book");
            taskManager.addDeadline("return book", "15-10-2019 18:00");
            taskManager.addEvent("project meeting", "15-10-2019 09:00", "15-10-2019 10:30");
            assertFileContents(temporaryFile, List.of(
                    "T | 0 | read book",
                    "D | 0 | return book | 15-10-2019 18:00",
                    "E | 0 | project meeting | 15-10-2019 09:00 | 15-10-2019 10:30"));

            taskManager.markTask(1);
            assertFileContents(temporaryFile, List.of(
                    "T | 1 | read book",
                    "D | 0 | return book | 15-10-2019 18:00",
                    "E | 0 | project meeting | 15-10-2019 09:00 | 15-10-2019 10:30"));

            taskManager.unmarkTask(1);
            taskManager.deleteTask(2);
            assertFileContents(temporaryFile, List.of(
                    "T | 0 | read book",
                    "E | 0 | project meeting | 15-10-2019 09:00 | 15-10-2019 10:30"));

            TaskList loadedTaskList = new TaskList(storage.load());
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
