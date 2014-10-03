package fr.vergne.benchmark.util.task;

import java.util.HashMap;
import java.util.Map;

import fr.vergne.benchmark.Benchmark;
import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.OutputGetter;
import fr.vergne.benchmark.Task;
import fr.vergne.benchmark.impl.AbstractSimpleTask;

/**
 * This {@link IdentityTask} {@link Task} implements an "intermediary task": it
 * receive a single input which is directly provided as output. The main use of
 * such class is for a {@link Benchmark} input, which is a 1-to-1 link. Using
 * this {@link Task} allows to receive the input and link this {@link Task} to
 * multiple others to make a 1-to-N link.<br/>
 * <br/>
 * Ideally, a {@link Benchmark} input should be a 1-to-N link, but it is not
 * possible in the current implementation. A future version of this library
 * should improve this point.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
// FIXME test or improve the benchmark class for 1-to-N inputs
public class IdentityTask<T> extends AbstractSimpleTask {

	public static final Object CONTENT = "content";
	private T content = null;
	public static final Object RESET = "reset";

	@Override
	protected Map<Object, InputSetter<?>> getInputs() {
		Map<Object, InputSetter<?>> inputs = new HashMap<Object, InputSetter<?>>();
		inputs.put(CONTENT, new InputSetter<T>() {

			@Override
			public void set(T input) {
				content = input;
			}
		});
		return inputs;
	}

	@Override
	protected Object getResetInputId() {
		return RESET;
	}

	@Override
	protected Map<Object, OutputGetter<?>> getOutputs() {
		Map<Object, OutputGetter<?>> outputs = new HashMap<Object, OutputGetter<?>>();
		outputs.put(CONTENT, new OutputGetter<T>() {

			@Override
			public T get() {
				return content;
			}

			@Override
			public boolean isSet() {
				return content != null;
			}
		});
		return outputs;
	}

	@Override
	protected void doExecute() {
		// nothing to do
	}

}
