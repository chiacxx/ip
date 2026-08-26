import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Stores tasks in insertion order, provides one-based lookup, and persists them.
 */
public class TaskList {
    /** File used to persist tasks, relative to the project root. */
    private static final Path DEFAULT_FILE_PATH = Paths.get(".", "data", "echo.txt");

    /** The tasks currently in the user's list. */
    private final List<Task> tasks = new ArrayList<>();
    /** The file used to load and save this task list. */
    private final Path filePath;

    /**
     * Creates a task list and loads any previously saved tasks.
     */
    public TaskList() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates a task list backed by a specific file.
     *
     * @param filePath File to load from and save to.
     */
    public TaskList(Path filePath) {
        this.filePath = filePath;
        load();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
        save();
    }

    /**
     * Removes and returns a task using its one-based task number.
     *
     * @param taskNumber One-based task number.
     * @return The removed task.
     */
    public Task removeTask(int taskNumber) {
        Task removedTask = tasks.remove(taskNumber - 1);
        save();
        return removedTask;
    }

    /**
     * Returns a task using its one-based task number.
     *
     * @param taskNumber One-based task number.
     * @return Task at that position.
     */
    public Task getTask(int taskNumber) {
        return tasks.get(taskNumber - 1);
    }

    /**
     * Checks whether a task number is available.
     *
     * @param taskNumber One-based task number.
     * @return Whether the task exists.
     */
    public boolean hasTask(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    /**
     * Returns the number of stored tasks.
     *
     * @return Number of stored tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether the list contains no tasks.
     *
     * @return Whether the list is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns a read-only snapshot of the tasks in insertion order.
     *
     * @return Immutable task snapshot.
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }

    /**
     * Saves all current tasks, replacing the previous contents of the file.
     *
     * @throws UncheckedIOException If the file cannot be written.
     */
    public void save() {
        List<String> lines = tasks.stream()
                .map(Task::toFileFormat)
                .toList();

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to save tasks to " + filePath, exception);
        }
    }

    /** Loads saved tasks if a persistence file already exists. */
    private void load() {
        if (!Files.exists(filePath)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
                loadTask(line);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to load tasks from " + filePath, exception);
        }
    }

    /** Creates and adds one task from a saved line. */
    private void loadTask(String line) {
        String[] fields = line.split("\\s*\\|\\s*", -1);
        if (fields.length < 3) {
            return;
        }

        String description = fields[2];
        Task task;
        switch (fields[0]) {
        case "T":
            task = new TodoTask(description);
            break;
        case "D":
            if (fields.length < 4) {
                return;
            }
            task = new DeadlineTask(description, fields[3]);
            break;
        case "E":
            if (fields.length < 5) {
                return;
            }
            task = new EventTask(description, fields[3], fields[4]);
            break;
        default:
            return;
        }

        if ("1".equals(fields[1])) {
            task.mark();
        }
        tasks.add(task);
    }
}
