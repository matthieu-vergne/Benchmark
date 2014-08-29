package fr.vergne.benchmark.impl;

import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.Link;
import fr.vergne.benchmark.Task;

/**
 * A {@link ForcedLink} aims at setting the input of a {@link Task} with a fixed
 * value.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class ForcedLink<Type> implements Link<Type> {

	private final Task sourceTask;
	private final Task targetTask;
	private final Type value;
	private final Object targetId;

	public ForcedLink(Task from, Type value, Task to, Object inputId) {
		this.sourceTask = from;
		this.targetTask = to;
		this.value = value;
		this.targetId = inputId;
	}

	@Override
	public Task getSourceTask() {
		return sourceTask;
	}

	@Override
	public Task getTargetTask() {
		return targetTask;
	}

	@Override
	public Object getTargetId() {
		return targetId;
	}

	public InputSetter<Type> getTarget() {
		return targetTask.getInput(targetId);
	}

	@Override
	public Type getValue() {
		return value;
	}

	@Override
	public boolean isTransferable() {
		return true;
	}

	@Override
	public void transfer() {
		getTarget().set(getValue());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof ForcedLink) {
			ForcedLink<?> l = (ForcedLink<?>) obj;
			return l.sourceTask == sourceTask && l.targetTask == targetTask
					&& l.targetId == targetId;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return sourceTask.hashCode() * targetTask.hashCode()
				* targetId.hashCode();
	}

	@Override
	public String toString() {
		return sourceTask + " -> " + value + " -> " + targetTask + "("
				+ targetId + ")";
	}
}
