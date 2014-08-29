package fr.vergne.benchmark;

import java.util.Collection;
import java.util.LinkedHashSet;

import fr.vergne.benchmark.event.BenchmarkEvent;
import fr.vergne.benchmark.event.BenchmarkEventListener;
import fr.vergne.benchmark.event.LinkTransferedEvent;
import fr.vergne.benchmark.event.TaskExecutedEvent;
import fr.vergne.benchmark.event.TaskFailedEvent;
import fr.vergne.benchmark.event.TaskSelectedEvent;

/**
 * A {@link BenchmarkRunner} aims at running a {@link Benchmark} step by step.
 * Each time the {@link #run()} method is called, a {@link Task} of the
 * {@link Benchmark} is selected and executed. During each single run, several
 * {@link BenchmarkEvent}s are generated and can be catched by registering a
 * listener with {@link #registerListener(BenchmarkEventListener)}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class BenchmarkRunner {

	private Benchmark benchmark = null;
	private Collection<BenchmarkEventListener> listeners = new LinkedHashSet<BenchmarkEventListener>();

	public BenchmarkRunner() {
	}

	public BenchmarkRunner(Benchmark benchmark) {
		setBenchmark(benchmark);
	}

	public void setBenchmark(Benchmark benchmark) {
		this.benchmark = benchmark;
	}

	public Benchmark getBenchmark() {
		return benchmark;
	}

	/**
	 * Execute a single {@link Task} of the {@link Benchmark} managed by this
	 * {@link BenchmarkRunner}. During the process, several
	 * {@link BenchmarkEvent}s are generated:
	 * <ol>
	 * <li>A {@link TaskSelectedEvent} which provides the selected {@link Task},
	 * </li>
	 * <li>A {@link TaskExecutedEvent} if the execution of the {@link Task} runs
	 * well,</li>
	 * <li>A {@link TaskFailedEvent} if the {@link Task} fail to be executed
	 * fully,</li>
	 * <li>A {@link LinkTransferedEvent} for each {@link Link} transferred.</li>
	 * </ol>
	 * 
	 * @throws NoTaskToRunException
	 *             if no {@link Task} of the {@link Benchmark} can be selected
	 *             for execution
	 * @throws FailedTaskException
	 *             if the {@link Task} selected has generated an exception
	 */
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
