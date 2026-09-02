package echo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import echo.storage.Storage;
import echo.ui.Ui;

/**
 * Tests the response generation and state tracking of {@link Echo}.
 */
public class EchoTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getWelcomeMessage_returnsNonEmptyGreeting() {
        Echo echo = createEcho();

        assertTrue(echo.getWelcomeMessage().contains("Online and listening"));
    }

    @Test
    public void getResponse_validTodoCommand_returnsAddedMessage() {
        Echo echo = createEcho();

        String response = echo.getResponse("todo read book");

        assertTrue(response.contains("Added the following task:"));
        assertTrue(response.contains("read book"));
        assertFalse(echo.isExit());
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        Echo echo = createEcho();

        String response = echo.getResponse("invalidcommand");

        assertTrue(response.contains("do not recognise 'invalidcommand'"));
        assertFalse(echo.isExit());
    }

    @Test
    public void getResponse_exitCommand_setsExitFlag() {
        Echo echo = createEcho();

        String response = echo.getResponse("bye");

        assertFalse(response.isBlank());
        assertTrue(echo.isExit());
    }

    private Echo createEcho() {
        return new Echo(new Ui(), new Storage(temporaryDirectory.resolve("echo_test.txt")));
    }
}
