package fr.vergne.benchmark;

import java.util.Collection;
import java.util.LinkedHashSet;

import fr.vergne.benchmark.event.BenchmarkEvent;
import fr.vergne.benchmark.event.BenchmarkEventListener;
import fr.vergne.benchmark.event.LinkTransferedEvent;
import fr.vergne.benchmark.event.TaskExecutedEvent;
import fr.vergne.benchmark.event.TaskFailedEvent;
import fr.vergne.benchmark.event.TaskSelectedEvent;


public class BenchmarkRunner {

	private Benchmark benchmark = null;
	private Collection<BenchmarkEventListener> listeners = new LinkedHashSet<BenchmarkEventListener>();

	public void setBenchmark(Benchmark benchmark) {
		this.benchmark = benchmark;
	}
	
	public Benchmark getBenchmark() {
		return benchmark;
	}

	public void run() throws NoTaskToRunException {
		Task task = selectTask();
		if (task == null) {
			throw new NoTaskToRunException(benchmark);
		} else {
			spreadEvent(new TaskSelectedEvent(task));
			try {
				task.execute();
				spreadEvent(new TaskExecutedEvent(task));
			} catch (Exception ex) {
				spreadEvent(new TaskFailedEvent(task, ex));
				throw new FailedTaskException(task, ex);
			}
			for (Link<?> link : benchmark.getLinks()) {
				if (link.getSourceTask() == task && link.isTransferable()) {
					link.transfer();
					spreadEvent(new LinkTransferedEvent(link));
				} else {
					// unrelated link
				}
			}
		}
	}

	public void registerListener(BenchmarkEventListener listener) {
		listeners.add(listener);
	}

	public void unregisterListener(BenchmarkEventListener listener) {
		listeners.remove(listener);
	}

	private void spreadEvent(BenchmarkEvent event) {
		for (BenchmarkEventListener listener : listeners) {
			listener.eventGenerated(event);
		}
	}

	private Task selectTask() {
		for (Task task : benchmark.getTasks()) {
			if (task.isExecutable() && task.shouldBeExecuted()) {
				return task;
			} else {
				continue;
			}
		}
		return null;
	}
}
