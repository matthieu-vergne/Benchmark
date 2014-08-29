package fr.vergne.benchmark.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.vergne.benchmark.BenchmarkBuilder;
import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.OutputGetter;
import fr.vergne.benchmark.Task;

/**
 * Simple implementation for {@link Task}s which need to have all their inputs
 * set before to be executed. No check is made on the values set: as long as the
 * {@link InputSetter#set(Object)} method is called, the input is considered as
 * set, even with a <code>null</code> value.<br/>
 * <br/>
 * If the {@link Task} should be re-executed, a reset input can be added by
 * using the {@link #getResetInputId()} method. If so, it is the only input
 * which does not need to be set for the {@link Task} to be executed. Setting
 * this input always correspond to a reset, independently of the value provided.
 * The value is a {@link Collection} of input IDs to reset. If no ID is
 * provided, the {@link Task} can be re-executed immediately, otherwise the
 * corresponding inputs need to be re-generated before to execute the
 * {@link Task} again.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public abstract class AbstractSimpleTask implements Task {

	private boolean shouldExecute = true;
	private final Collection<Object> setInputs = new HashSet<Object>();

	public AbstractSimpleTask() {
	}

	/**
	 * 
	 * @return the set of inputs to consider for this {@link Task}
	 * @see #getResetInputId()
	 */
	protected abstract Map<Object, InputSetter<?>> getInputs();

	/**
	 * 
	 * @return the ID of the reset input, <code>null</code> if no reset input
	 *         should be provided
	 */
	protected abstract Object getResetInputId();

	@Override
	public Collection<Object> getInputIds() {
		return buildInputs().keySet();
	}

	private Map<Object, InputSetter<?>> buildInputs() {
		Map<Object, InputSetter<?>> inputs;
		inputs = new HashMap<Object, InputSetter<?>>();
		inputs.putAll(getInputs());

		Object resetId = getResetInputId();
		if (resetId != null) {
			inputs.put(resetId, new InputSetter<Collection<Object>>() {

				@Override
				public void set(Collection<Object> input) {
					setInputs.removeAll(input);
					shouldExecute = true;
				}
			});
		} else {
			// no reset input
		}
		return inputs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <In> InputSetter<In> getInput(final Object id) {
		Map<Object, InputSetter<?>> inputs = buildInputs();
		if (inputs.containsKey(id)) {
			final InputSetter<In> inputSetter = (InputSetter<In>) inputs
					.get(id);
			InputSetter<In> wrapper = new InputSetter<In>() {

				@Override
				public void set(In input) {
					inputSetter.set(input);
					setInputs.add(id);
				}
			};
			return wrapper;
		} else {
			throw new NoSuchElementException();
		}
	}

	protected abstract Map<Object, OutputGetter<?>> getOutputs();

	@Override
	public Collection<Object> getOutputIds() {
		return getOutputs().keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Out> OutputGetter<Out> getOutput(Object id) {
		if (getOutputIds().contains(id)) {
			return (OutputGetter<Out>) getOutputs().get(id);
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public boolean shouldBeExecuted() {
		return shouldExecute;
	}

	protected void reset() {
		shouldExecute = true;
	}

	@Override
	public boolean isExecutable() {
		return setInputs.containsAll(getInputs().keySet());
	}

	/**
	 * This method should implement the actual process of this {@link Task}.<br/>
	 * <br/>
	 * NB: This {@link Task} is considered executed when the
	 * {@link #doExecute()} method is finished. Consequently, if you request a
	 * reset of the {@link Task} during its own process, it will be overridden
	 * at the end of its execution. Use a {@link ForcedLink} or the method
	 * {@link BenchmarkBuilder#linkValue(Task, Object, Task, Object)} to request
	 * the reset once the {@link Task} has finished its execution.
	 */
	protected abstract void doExecute();

	@Override
	public void execute() {
		doExecute();
		shouldExecute = false;
	}

}
