package fr.vergne.benchmark.event;

import fr.vergne.benchmark.Task;

public class TaskExecutedEvent implements BenchmarkEvent {

	private Task task;

	public TaskExecutedEvent(Task task) {
		this.task = task;
	}

	public Task getTask() {
		return task;
	}

}
