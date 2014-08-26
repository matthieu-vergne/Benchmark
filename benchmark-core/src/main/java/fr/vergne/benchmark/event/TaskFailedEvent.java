package fr.vergne.benchmark.event;

import fr.vergne.benchmark.Task;

public class TaskFailedEvent implements BenchmarkEvent {

	private final Task task;
	private final Exception cause;

	public TaskFailedEvent(Task task, Exception cause) {
		this.task = task;
		this.cause = cause;
	}

	public Task getTask() {
		return task;
	}

	public Exception getCause() {
		return cause;
	}
}
