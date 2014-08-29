package fr.vergne.benchmark.impl;

import java.util.NoSuchElementException;

import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.Link;
import fr.vergne.benchmark.OutputGetter;
import fr.vergne.benchmark.Task;

/**
 * A {@link SyncLink} aims at linking the output of a {@link Task} to the input
 * of another. The value of the output is transfered to the value of the input.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class SyncLink<Type> implements Link<Type> {

	private final Task sourceTask;
	private final Task targetTask;
	private final Object sourceId;
	private final Object targetId;

	public SyncLink(Task from, Object outputId, Task to, Object inputId) {
		this.sourceTask = from;
		this.targetTask = to;
		this.sourceId = outputId;
		this.targetId = inputId;
	}

	@Override
	public Task getSourceTask() {
		return sourceTask;
	}

	public Object getSourceId() {
		return sourceId;
	}

	public OutputGetter<Type> getSource() {
		return sourceTask.getOutput(sourceId);
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
		if (isTransferable()) {
			return getSource().get();
		} else {
			throw new NoSuchElementException(
					"No value available for a link which is not transferable.");
		}
	}

	@Override
	public boolean isTransferable() {
		return getSource().isSet();
	}

	@Override
	public void transfer() {
		getTarget().set(getValue());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof SyncLink) {
			SyncLink<?> l = (SyncLink<?>) obj;
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

	@Override
	public String toString() {
		return sourceTask + "(" + sourceId + ") -> " + targetTask + "("
				+ targetId + ")";
	}
}
