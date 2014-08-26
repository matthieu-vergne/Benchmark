package fr.vergne.benchmark;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import fr.vergne.benchmark.impl.ImmutableBenchmark;

/**
 * Builder to facilitate the creation of a {@link Benchmark}. All the methods
 * return the current builder to facilitate call chaining, excepted
 * {@link #createInstance()} which returns the built {@link Benchmark}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class BenchmarkBuilder {

	private final Collection<Task> tasks = new LinkedHashSet<Task>();
	private final Collection<Link<?>> links = new LinkedHashSet<Link<?>>();

	/**
	 * Calling {@link #createInstance()} just after calling this method create
	 * an empty {@link Benchmark}.
	 */
	public BenchmarkBuilder clear() {
		links.clear();
		tasks.clear();
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
	 * {@link Task} is still linked to others, an exception is thrown. You can
	 * call {@link #unlinkAll(Task)} to ensure this case does not happen.
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
	 * This method links two {@link Task}s previously added. The {@link Link} is
	 * not re-made if it has been made with a previous call.
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
	public <T> BenchmarkBuilder link(Task from, Object outputId, Task to,
			Object inputId) {
		if (!tasks.contains(from)) {
			throw new IllegalArgumentException(
					"The source task has not been added: " + from);
		} else if (!tasks.contains(to)) {
			throw new IllegalArgumentException(
					"The target task has not been added: " + to);
		} else {
			links.add(new Link<T>(from, outputId, to, inputId));
			return this;
		}
	}

	/**
	 * This method removes a previously made {@link Link}. If such a link does
	 * not exist, nothing happen.
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
	public <T> BenchmarkBuilder unlink(Task from, Object outputId, Task to,
			Object inputId) {
		links.remove(new Link<T>(from, outputId, to, inputId));
		return this;
	}

	/**
	 * This method removes all the links which relates a given {@link Task}.
	 * After calling this method, the {@link Task}'s inputs and outputs are not
	 * linked anymore, but the {@link Task} is still part of the
	 * {@link Benchmark}. Use {@link #remove(Task)} to remove it once unlinked.
	 * 
	 * @param task
	 *            the {@link Task} to unlink
	 */
	public <T> BenchmarkBuilder unlinkAll(Task task) {
		Iterator<Link<?>> iterator = links.iterator();
		while (iterator.hasNext()) {
			Link<?> link = iterator.next();
			if (link.getSourceTask() == task || link.getTargetTask() == task) {
				iterator.remove();
			} else {
				// unrelated link
			}
		}
		return this;
	}

	/**
	 * This method remove all the {@link Task}s which are not linked anymore.
	 */
	public <T> BenchmarkBuilder clean() {
		Collection<Task> retained = new HashSet<Task>();
		for (Link<?> link : links) {
			retained.add(link.getSourceTask());
			retained.add(link.getTargetTask());
		}
		tasks.retainAll(retained);
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
		return new ImmutableBenchmark(tasks, links);
	}

}
