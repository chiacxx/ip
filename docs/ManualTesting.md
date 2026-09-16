# E.C.H.O. Manual Testing Guide & Matrix

This guide provides instructions and checklists for manual testing of components and system configurations that cannot be tested effectively via automated JUnit tests.

---

## 1. Scope of Manual Testing

While headless business logic, parsers, storage, models, and command executions are covered by automated JUnit test suites, manual verification is required for:
1. **Graphical User Interface (JavaFX)**: Visual alignment, dialog box wrapping, avatar loading, scrolling behavior, and UI scaling.
2. **Cross-Platform Compatibility**: Operating system differences between Windows, macOS, and Linux.
3. **Screen Resolutions & DPI Scaling**: Layout consistency across different monitor resolutions and OS display scale factors (100% to 200%).
4. **Operating System Language & Locale Settings**: Handling non-Latin character sets (e.g., Chinese inputs) and ensuring date formatting consistency across locales.
5. **Command-Line Interface (CLI)**: Interactive terminal experience and terminal signal handling (e.g., EOF / `Ctrl+D` / `Ctrl+Z`).

---

## 2. Platform & Environment Test Matrix

| Test Environment | OS Version | Display Resolution | Scaling | OS Language / Locale | Status |
|---|---|---|---|---|---|
| Environment 1 | Windows 11 | 1920 x 1080 (FHD) | 100% | English (United States) | [ ] |
| Environment 2 | Windows 11 | 2560 x 1440 (QHD) | 125% / 150% | Chinese (Simplified - China) | [ ] |
| Environment 3 | macOS (Sonoma / Sequoia) | Retina (2880 x 1800) | Default (200% HiDPI) | English (United Kingdom / Singapore) | [ ] |
| Environment 4 | macOS (Sonoma / Sequoia) | 1920 x 1080 | 100% | Chinese (Traditional - Taiwan) | [ ] |
| Environment 5 | Linux (Ubuntu 24.04 LTS) | 1920 x 1080 | 100% | English (US) | [ ] |

---

## 3. Test Cases & Verification Procedures

### Category A: Graphical User Interface (JavaFX)

#### Test Case A1: Startup and Visual Assets
- **Procedure**: Run `./gradlew run` or execute the shadow JAR `java -jar build/libs/echo.jar`.
- **Expected Outcome**:
  - The window opens with title `E.C.H.O. // Everyday Conversational & Helpful Operator`.
  - The initial E.C.H.O. greeting message is displayed on the left with the bot profile image (`DaDuke.png`).
  - No missing image exceptions or blank placeholder squares appear.
  - The send button and input text field are visible, active, and properly aligned at the bottom.

#### Test Case A2: Text Wrapping in Dialog Boxes
- **Procedure**:
  1. Input a command with a long sentence: `todo This is a very long directive description intended to verify that dialogue bubbles expand vertically and wrap text nicely without horizontal clipping or leaking outside the bubble boundaries`.
  2. Input a command with a long unbroken word: `todo AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA`.
  3. Input `help`.
- **Expected Outcome**:
  - The user's input bubble displays on the right with the user icon (`DaUser.png`).
  - E.C.H.O.'s response bubble displays on the left with the bot icon.
  - Text wraps within the dialog box. No text is cut off or truncated horizontally.
  - Multi-line responses (such as the `help` manual) format with consistent line spacing and indentation.

#### Test Case A3: Auto-Scroll Behavior
- **Procedure**: Add 10 or more tasks consecutively using `todo task 1`, `todo task 2`, ..., `todo task 10`.
- **Expected Outcome**:
  - As new dialog boxes are added and the conversation exceeds the visible height, the `ScrollPane` automatically scrolls down to the most recent response.
  - The user can manually scroll up to review previous messages.

#### Test Case A4: Window Resizing and Responsiveness
- **Procedure**:
  1. Drag the window corners to resize the window horizontally and vertically.
  2. Maximize the application window.
  3. Minimize the application window to minimum supported dimensions.
- **Expected Outcome**:
  - The scroll view expands/contracts proportionally.
  - The input text field and `Send` button remain anchored at the bottom edge.
  - Existing message bubbles adapt to the resized container width without clipping.

#### Test Case A5: Clean Shutdown
- **Procedure**: Input `bye` into the chat input.
- **Expected Outcome**:
  - E.C.H.O. outputs a farewell transmission message.
  - The application terminates gracefully and closes the GUI window.

---

### Category B: Screen Resolutions & DPI Scaling

#### Test Case B1: Standard Scaling (100% on 1080p / 1440p)
- **Settings**: System display scale set to 100%.
- **Expected Outcome**: Text, icons, and buttons are crisp and legible. UI components are neither oversized nor cramped.

#### Test Case B2: High DPI Scaling (125%, 150%, 200%)
- **Settings**: Windows display settings set to 125% or 150%; macOS Retina displays (HiDPI 200%).
- **Expected Outcome**:
  - UI elements scale cleanly according to OS scale factor without pixelation or blurry font rendering.
  - Layout margins and padding remain proportional. No overlapping between input bar and dialog list.

---

### Category C: Language & Locale Settings (English vs Chinese)

#### Test Case C1: Chinese Character Input and Display
- **Procedure**:
  1. Input `todo 准备计算机网络复习笔记`.
  2. Input `deadline 提交软件工程项目报告 /by 15-10-2026 23:59`.
  3. Input `event 科技创新与创业学术研讨会 /from 20-10-2026 09:00 /to 22-10-2026 17:00`.
  4. Input `list`.
- **Expected Outcome**:
  - Chinese characters render clearly without mojibake (garbled characters) or square boxes (`\uFFFD`).
  - Output displays formatted dates correctly in English (`Oct 15 2026, 11:59pm`) as specified by `Locale.ENGLISH`.

#### Test Case C2: Keyword Search with Non-Latin Characters
- **Procedure**:
  1. With the tasks from Test Case C1 added, input `find 计算机`.
  2. Input `find 研讨会`.
  3. Input `find 论文` (non-matching keyword).
- **Expected Outcome**:
  - Searching for `计算机` returns only the matching task `准备计算机网络复习笔记`.
  - Searching for non-matching keyword returns `No directives found matching keyword: '论文'.`.

#### Test Case C3: Storage File Encoding under Different OS Locales
- **Procedure**:
  1. Run the application on an OS configured with Chinese locale (e.g. `zh-CN` with GBK default system codepage).
  2. Add tasks with Chinese descriptions.
  3. Exit the application (`bye`).
  4. Inspect `./data/echo.txt` using a text editor (e.g., VS Code or Notepad).
  5. Restart the application.
- **Expected Outcome**:
  - The file `./data/echo.txt` is encoded in UTF-8 (`StandardCharsets.UTF_8`).
  - Reloading tasks preserves Chinese characters accurately without corruption.

---

### Category D: Command-Line Interface (CLI)

#### Test Case D1: Interactive Terminal Execution
- **Procedure**: Run `./gradlew run --console=plain` in a terminal window (PowerShell / Command Prompt / Bash / Zsh).
- **Expected Outcome**:
  - Terminal prints the ASCII banner and greeting.
  - Input prompt `[E.C.H.O. // SYS] >> ` appears on standard output.
  - Commands (`todo`, `deadline`, `event`, `list`, `sort`, `mark`, `delete`, `help`) execute correctly.
  - Typing `bye` terminates the CLI session with exit code 0.

#### Test Case D2: Terminal Signal Handling (EOF)
- **Procedure**: Launch CLI mode, then press `Ctrl+Z` (Windows) or `Ctrl+D` (Unix/macOS) to send an EOF signal.
- **Expected Outcome**:
  - E.C.H.O. terminates cleanly without uncaught exceptions or hanging processes.

---

## 4. Manual Testing Checklist

| No. | Description | Tested On (OS & Resolution) | Result (Pass / Fail) | Remarks |
|---|---|---|---|---|
| 1 | Welcome banner & avatar display | | | |
| 2 | DialogBox text wrapping for long text | | | |
| 3 | Auto-scrolling to newest response | | | |
| 4 | Window maximize and resize fluidity | | | |
| 5 | Clean exit upon `bye` command | | | |
| 6 | High-DPI (125% / 150% / 200%) rendering | | | |
| 7 | Chinese input, listing, and storage | | | |
| 8 | Chinese keyword search (`find`) | | | |
| 9 | UTF-8 persistence integrity across reboots | | | |
| 10 | Interactive CLI input and EOF termination | | | |
