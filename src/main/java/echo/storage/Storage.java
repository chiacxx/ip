package echo.storage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import echo.task.DeadlineTask;
import echo.task.EventTask;
import echo.task.Task;
import echo.task.TodoTask;

/**
 * Loads tasks from and saves tasks to E.C.H.O.'s persistence file.
 */
public class Storage {
    /** File used by the default application instance. */
    private static final Path DEFAULT_FILE_PATH = Paths.get(".", "data", "echo.txt");

    /** File used to persist tasks. */
    private final Path filePath;

    /**
     * Creates a new Storage instance using the application's default persistence file.
     */
    public Storage() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates a new Storage instance using a given persistence filepath.
     *
     * @param filePath file used to load and save tasks.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all valid tasks from the persistence file.
     *
     * @return tasks loaded from the file, in insertion order.
     * @throws UncheckedIOException if the file cannot be read.
     */
    public List<Task> load() {
        if (!Files.exists(filePath)) {
            return List.of();
        }

        try {
            List<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            return tasks;
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to load tasks from " + filePath, exception);
        }
    }

    /**
     * Saves all tasks, replacing the previous contents of the persistence file.
     *
     * @param tasks tasks to save.
     * @throws UncheckedIOException if the file cannot be written.
     */
    public void save(List<Task> tasks) {
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

    /**
     * Creates one task from a saved line in the storage file.
     *
     * @param line String entry representing one task.
     * @return {@link Task} object.
     */
    private Task parseTask(String line) {
        String[] fields = line.split("\\s*\\|\\s*", -1);
        if (fields.length < 3) {
            return null;
        }

        String description = fields[2];
        Task task;
        switch (fields[0]) {
        case "T":
            task = new TodoTask(description);
            break;
        case "D":
            if (fields.length < 4) {
                return null;
            }
            task = new DeadlineTask(description, fields[3]);
            break;
        case "E":
            if (fields.length < 5) {
                return null;
            }
            task = new EventTask(description, fields[3], fields[4]);
            break;
        default:
            return null;
        }

        if ("1".equals(fields[1])) {
            task.mark();
        }
        return task;
    }
}
