# E.C.H.O. project

This is a project template for a greenfield Java project for the E.C.H.O. chatbot. E.C.H.O. stands for _Everyday Conversational and Helpful Operator_. Given below are instructions on how to use it.

## Setting up in IntelliJ

Prerequisites: JDK 25 and the latest version of IntelliJ.

1. Open IntelliJ. If another project is open, click `File` > `Close Project` first.
1. Open the project in IntelliJ:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** as explained in the [IntelliJ documentation](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. Locate `src/main/java/Echo.java`, right-click it, and choose `Run Echo.main()`.
   E.C.H.O. should start and display its welcome message.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Commands

Run `help` in E.C.H.O. to display the available commands. The supported formats are:

```text
help
list
todo <description>
deadline <description> /by <deadline>
event <description> /from <start> /to <end>
mark <task number>
unmark <task number>
delete <task number>
bye
```

Task numbers are the one-based numbers shown by `list`. Invalid commands and
task formats are reported with an explanation and an example of how to fix them.
