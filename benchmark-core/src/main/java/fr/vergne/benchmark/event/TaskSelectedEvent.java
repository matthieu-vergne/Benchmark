package fr.vergne.benchmark.event;

import fr.vergne.benchmark.Task;

public class TaskSelectedEvent implements BenchmarkEvent {

	private final Task task;

	public TaskSelectedEvent(Task task) {
		this.task = task;
	}

	public Task getTask() {
		return task;
	}
}
