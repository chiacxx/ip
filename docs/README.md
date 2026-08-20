# E.C.H.O. User Guide

E.C.H.O. is a command-line task manager for adding and tracking everyday
tasks.

## Quick start

Start the application, then enter `help` to see the available operations.
Commands are case-insensitive.

## Adding tasks

### Todo

Use a todo for a task without a time requirement:

```text
todo buy groceries
```

### Deadline

Use `/by` to separate the task description from its deadline:

```text
deadline submit report /by Friday
```

### Event

Use `/from` and `/to` to describe an event's time range:

```text
event team meeting /from 2pm /to 3pm
```

## Managing tasks

```text
list
mark 1
unmark 1
```

`list` displays every task and its one-based task number. Use that number
with `mark` or `unmark`.

## Ending the session

Enter `bye` to disconnect from E.C.H.O. Invalid commands are explained with
the relevant command format so they can be corrected immediately.
