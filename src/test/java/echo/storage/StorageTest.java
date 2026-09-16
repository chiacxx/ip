package echo.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import echo.EchoException;
import echo.task.Task;
import echo.task.TaskList;
import echo.task.TaskManager;

/**
 * Tests error resilience and file operations in {@link Storage}.
 */
public class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void load_corruptedLinesInFile_skipsCorruptedLinesWithoutCrashing() throws IOException {
        Path filePath = temporaryDirectory.resolve("corrupted.txt");
        List<String> rawLines = List.of(
                "T | 0 | valid todo",
                "D | 0 | corrupted deadline | 31-02-2026", // Invalid date
                "UNKNOWN | 0 | invalid type",
                "E | 0 | incomplete event | 01-10-2026", // Missing end date
                "E | 0 | invalid chronology event | 05-10-2026 | 01-10-2026", // Start after end
                "T | 1 | another valid todo"
        );
        Files.write(filePath, rawLines, StandardCharsets.UTF_8);

        Storage storage = new Storage(filePath);
        List<Task> loadedTasks = storage.load();

        assertEquals(2, loadedTasks.size());
        assertEquals("valid todo", loadedTasks.get(0).getDescription());
        assertEquals("another valid todo", loadedTasks.get(1).getDescription());
        assertTrue(loadedTasks.get(1).isDone());
        assertTrue(storage.hasCorruptedEntries());
    }

    @Test
    public void save_whenCorruptedEntriesExist_createsBackupFile() throws IOException {
        Path filePath = temporaryDirectory.resolve("with_corrupt.txt");
        List<String> rawLines = List.of(
                "T | 0 | keep me",
                "CORRUPT | DATA"
        );
        Files.write(filePath, rawLines, StandardCharsets.UTF_8);

        Storage storage = new Storage(filePath);
        List<Task> loadedTasks = storage.load();
        assertTrue(storage.hasCorruptedEntries());

        storage.save(loadedTasks);

        Path backupPath = temporaryDirectory.resolve("with_corrupt.txt.corrupted.bak");
        assertTrue(Files.exists(backupPath));
        List<String> backupLines = Files.readAllLines(backupPath, StandardCharsets.UTF_8);
        assertEquals(rawLines, backupLines);

        assertFalse(storage.hasCorruptedEntries());
    }

    @Test
    public void save_unwritableFile_throwsEchoExceptionFromTaskManager() throws IOException, EchoException {
        Path filePath = temporaryDirectory.resolve("readonly.txt");
        Files.write(filePath, List.of("T | 0 | initial"), StandardCharsets.UTF_8);

        File file = filePath.toFile();
        boolean isReadOnlySet = file.setReadOnly();
        if (!isReadOnlySet) {
            return; // Skip on platforms where setReadOnly is not supported
        }

        try {
            Storage storage = new Storage(filePath);
            TaskManager manager = new TaskManager(new TaskList(storage.load()), storage);

            EchoException exception = assertThrows(
                    EchoException.class, () -> manager.addTodo("fail to save"));
            assertTrue(exception.getMessage().contains("Storage failure: Unable to save changes"));
        } finally {
            file.setWritable(true);
        }
    }
}
