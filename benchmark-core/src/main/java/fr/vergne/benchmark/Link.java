package fr.vergne.benchmark;

import java.util.NoSuchElementException;

/**
 * A {@link Link} aims at linking the output of a {@link Task} to the input of
 * another.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class Link<Type> {

	private final Task sourceTask;
	private final Task targetTask;
	private final Object sourceId;
	private final Object targetId;

	public Link(Task from, Object outputId, Task to, Object inputId) {
		this.sourceTask = from;
		this.targetTask = to;
		this.sourceId = outputId;
		this.targetId = inputId;
	}

	public Task getSourceTask() {
		return sourceTask;
	}

	public Object getSourceId() {
		return sourceId;
	}

	public OutputGetter<Type> getSource() {
		return sourceTask.getOutput(sourceId);
	}

	public Task getTargetTask() {
		return targetTask;
	}

	public Object getTargetId() {
		return targetId;
	}

	public InputSetter<Type> getTarget() {
		return targetTask.getInput(targetId);
	}

	public Type getValue() {
		if (isTransferable()) {
			return getSource().get();
		} else {
			throw new NoSuchElementException(
					"No value available for a link which is not transferable.");
		}
	}

	public boolean isTransferable() {
		return getSource().isSet();
	}

	public void transfer() {
		getTarget().set(getValue());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof Link) {
			Link<?> l = (Link<?>) obj;
			return l.sourceTask == sourceTask && l.sourceId == sourceId
					&& l.targetTask == targetTask && l.targetId == targetId;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return sourceTask.hashCode() * sourceId.hashCode()
				* targetTask.hashCode() * targetId.hashCode();
	}
}
