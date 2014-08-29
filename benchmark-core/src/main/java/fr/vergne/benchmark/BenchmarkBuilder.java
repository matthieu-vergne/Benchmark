package fr.vergne.benchmark;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

import fr.vergne.benchmark.impl.ForcedLink;
import fr.vergne.benchmark.impl.ImmutableBenchmark;
import fr.vergne.benchmark.impl.SyncLink;
import fr.vergne.benchmark.util.IOIdentifier;

/**
 * Builder to facilitate the creation of a {@link Benchmark}. All the methods
 * return the current builder to facilitate call chaining, excepted
 * {@link #createInstance()} which returns the built {@link Benchmark}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
// TODO revise the tests to consider the benchmark inputs/outputs
public class BenchmarkBuilder {

	private final Collection<Task> tasks = new LinkedHashSet<Task>();
	private final Collection<Link<?>> links = new LinkedHashSet<Link<?>>();
	private final Map<Object, IOIdentifier> inputs = new HashMap<Object, IOIdentifier>();
	private final Map<Object, IOIdentifier> outputs = new HashMap<Object, IOIdentifier>();

	/**
	 * Calling {@link #createInstance()} just after calling this method create
	 * an empty {@link Benchmark}.
	 */
	public BenchmarkBuilder clear() {
		links.clear();
		tasks.clear();
		inputs.clear();
		outputs.clear();
		return this;
	}

	/**
	 * Add a {@link Task} which can be linked later. If the {@link Task} has
	 * already been added, it is not re-added.
	 * 
	 * @param task
	 *            the {@link Task} to add
	 */
	public BenchmarkBuilder add(Task task) {
		tasks.add(task);
		return this;
	}

	/**
	 * This method removes a {@link Task} from the {@link Benchmark}. If this
	 * {@link Task} is still linked to others or used as an input/output of the
	 * {@link Benchmark}, an exception is thrown. You can call
	 * {@link #unrelate(Task)} to ensure this case does not happen.
	 * 
	 * @param task
	 *            the {@link Task} to remove
	 * @throws IllegalArgumentException
	 *             if the {@link Task} cannot be removed because it is linked
	 */
	public <T> BenchmarkBuilder remove(Task task) {
		if (tasks.contains(task)) {
			for (Link<?> link : links) {
				if (link.getSourceTask() == task
						|| link.getTargetTask() == task) {
					throw new IllegalArgumentException(
							"The task cannot be removed: it is still linked to others.");
				} else {
					// unrelated link
				}
			}
			for (IOIdentifier id : inputs.values()) {
				if (id.getTask() == task) {
					throw new IllegalArgumentException(
							"The task cannot be removed: it is still used as input of the benchmark.");
				} else {
					// different task
				}
			}
			for (IOIdentifier id : outputs.values()) {
				if (id.getTask() == task) {
					throw new IllegalArgumentException(
							"The task cannot be removed: it is still used as output of the benchmark.");
				} else {
					// different task
				}
			}
			tasks.remove(task);
		} else {
			// nothing to remove
		}
		return this;
	}

	/**
	 * Convenient method for multiple {@link #add(Task)}.
	 */
	public BenchmarkBuilder addAll(Task... tasks) {
		for (Task task : tasks) {
			add(task);
		}
		return this;
	}

	/**
	 * Convenient method for multiple {@link #remove(Task)}.
	 */
	public BenchmarkBuilder removeAll(Task... tasks) {
		for (Task task : tasks) {
			remove(task);
		}
		return this;
	}

	/**
	 * This method links two {@link Task}s previously added. The
	 * {@link SyncLink} is not re-made if it has been made with a previous call.
	 * 
	 * @param from
	 *            the {@link Task} from which the output should be linked
	 * @param outputId
	 *            the ID of the output to link
	 * @param to
	 *            the {@link Task} to which the output should be linked
	 * @param inputId
	 *            the ID of the input to link
	 * @throws IllegalArgumentException
	 *             if the {@link Task}s have not been added
	 */
	public <T> BenchmarkBuilder linkOutput(Task from, Object outputId, Task to,
			Object inputId) {
		if (!tasks.contains(from)) {
			throw new IllegalArgumentException(
					"The source task has not been added: " + from);
		} else if (!tasks.contains(to)) {
			throw new IllegalArgumentException(
					"The target task has not been added: " + to);
		} else {
			links.add(new SyncLink<T>(from, outputId, to, inputId));
			return this;
		}
	}

	/**
	 * This method links two {@link Task}s previously added. The
	 * {@link ForcedLink} is not re-made if it has been made with a previous
	 * call.
	 * 
	 * @param from
	 *            the {@link Task} from which the output should be linked
	 * @param value
	 *            the value to transfer
	 * @param to
	 *            the {@link Task} to which the output should be linked
	 * @param inputId
	 *            the ID of the input to link
	 * @throws IllegalArgumentException
	 *             if the {@link Task}s have not been added
	 */
	// FIXME test
	public <T> BenchmarkBuilder linkValue(Task from, T value, Task to,
			Object inputId) {
		if (!tasks.contains(from)) {
			throw new IllegalArgumentException(
					"The source task has not been added: " + from);
		} else if (!tasks.contains(to)) {
			throw new IllegalArgumentException(
					"The target task has not been added: " + to);
		} else {
			links.add(new ForcedLink<T>(from, value, to, inputId));
			return this;
		}
	}

	/**
	 * This method removes a previously made {@link SyncLink}. If such a link
	 * does not exist, nothing happen.
	 * 
	 * @param from
	 *            the {@link Task} from which the output is linked
	 * @param outputId
	 *            the ID of the linked output
	 * @param to
	 *            the {@link Task} to which the output is linked
	 * @param inputId
	 *            the ID of the linked input
	 */
	public <T> BenchmarkBuilder unlinkOutput(Task from, Object outputId,
			Task to, Object inputId) {
		links.remove(new SyncLink<T>(from, outputId, to, inputId));
		return this;
	}

	/**
	 * This method removes a previously made {@link ForcedLink}. If such a link
	 * does not exist, nothing happen.
	 * 
	 * @param from
	 *            the {@link Task} from which the output is linked
	 * @param to
	 *            the {@link Task} to which the output is linked
	 * @param inputId
	 *            the ID of the linked input
	 */
	// FIXME test
	public <T> BenchmarkBuilder unlinkValue(Task from, Task to, Object inputId) {
		links.remove(new ForcedLink<T>(from, null, to, inputId));
		return this;
	}

	/**
	 * This method removes all the links which relates a given {@link Task} and
	 * remove it from the {@link Benchmark}'s inputs/outputs. After calling this
	 * method, the {@link Task}'s inputs and outputs are not linked anymore, but
	 * the {@link Task} is still part of the {@link Benchmark}. Use
	 * {@link #remove(Task)} to remove it once unlinked.
	 * 
	 * @param task
	 *            the {@link Task} to unlink
	 */
	public <T> BenchmarkBuilder unrelate(Task task) {
		Iterator<Link<?>> iterator = links.iterator();
		while (iterator.hasNext()) {
			Link<?> link = iterator.next();
			if (link.getSourceTask() == task || link.getTargetTask() == task) {
				iterator.remove();
			} else {
				// unrelated link
			}
		}
		Iterator<IOIdentifier> iterator2 = inputs.values().iterator();
		while (iterator2.hasNext()) {
			IOIdentifier id = iterator2.next();
			if (id.getTask() == task) {
				iterator2.remove();
			} else {
				// different task
			}
		}
		Iterator<IOIdentifier> iterator3 = outputs.values().iterator();
		while (iterator3.hasNext()) {
			IOIdentifier id = iterator3.next();
			if (id.getTask() == task) {
				iterator3.remove();
			} else {
				// different task
			}
		}
		return this;
	}

	/**
	 * This method is a convenient method to reduce the {@link Benchmark}. This
	 * method remove all the {@link Task}s which are not linked anymore. If they
	 * are used as inputs/outputs of the {@link Benchmark}, they are removed
	 * anyway and the corresponding input/output are forgotten.
	 */
	public <T> BenchmarkBuilder clean() {
		Collection<Task> retained = new HashSet<Task>();
		for (Link<?> link : links) {
			retained.add(link.getSourceTask());
			retained.add(link.getTargetTask());
		}
		Collection<Task> removed = new LinkedList<Task>(tasks);
		removed.removeAll(retained);
		for (Task task : removed) {
			unrelate(task);
		}
		tasks.retainAll(retained);
		return this;
	}

	/**
	 * Set an input for the {@link Benchmark}.
	 * 
	 * @param id
	 *            the ID of the {@link Benchmark}'s input
	 * @param task
	 *            the {@link Task} to use the input from
	 * @param taskInputId
	 *            the ID of the {@link Task}'s input used as input of the
	 *            {@link Benchmark}
	 */
	// FIXME test
	public BenchmarkBuilder setBenchmarkInput(Object id, Task task,
			Object taskInputId) {
		inputs.put(id, new IOIdentifier(task, taskInputId));
		return this;
	}

	/**
	 * Set an output for the {@link Benchmark}.
	 * 
	 * @param id
	 *            the ID of the {@link Benchmark}'s output
	 * @param task
	 *            the {@link Task} to use the output from
	 * @param taskOutputId
	 *            the ID of the {@link Task}'s output used as output of the
	 *            {@link Benchmark}
	 */
	// FIXME test
	public BenchmarkBuilder setBenchmarkOutput(Object id, Task task,
			Object taskOutputId) {
		outputs.put(id, new IOIdentifier(task, taskOutputId));
		return this;
	}

	/**
	 * This method return a new {@link Benchmark} instance for each call.
	 * Calling it twice in a row creates two different instances representing
	 * similar {@link Benchmark}s.
	 * 
	 * @return a new {@link Benchmark} based on all the {@link Task}s and links
	 *         previously added
	 */
	public Benchmark createInstance() {
		return new ImmutableBenchmark(tasks, links, inputs, outputs);
	}

}
