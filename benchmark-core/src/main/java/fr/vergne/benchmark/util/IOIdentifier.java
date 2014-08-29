package fr.vergne.benchmark.util;

import fr.vergne.benchmark.Task;

/**
 * An {@link IOIdentifier} is a couple ({@link Task}, ID) which identifies a
 * specific input or output.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class IOIdentifier {

	private final Task task;
	private final Object id;

	public IOIdentifier(Task task, Object id) {
		this.task = task;
		this.id = id;
	}

	public Task getTask() {
		return task;
	}

	public Object getId() {
		return id;
	}

	@Override
	public String toString() {
		return task + "(" + id + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof IOIdentifier) {
			IOIdentifier i = (IOIdentifier) obj;
			return task.equals(i.task) && id.equals(i.id);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return task.hashCode() * id.hashCode();
	}
}
