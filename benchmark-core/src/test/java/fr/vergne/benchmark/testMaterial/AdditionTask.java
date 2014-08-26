package fr.vergne.benchmark.testMaterial;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.OutputGetter;
import fr.vergne.benchmark.Task;

public class AdditionTask implements Task {

	public static final String RESULT = "result";
	private final Map<Object, Number> inputs = new HashMap<Object, Number>();
	private Number output = null;
	private boolean areInputsChanged = false;

	@Override
	public Collection<Object> getInputIds() {
		return inputs.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public InputSetter<Number> getInput(final Object id) {
		return new InputSetter<Number>() {

			@Override
			public void set(Number input) {
				Number old = inputs.put(id, input);
				areInputsChanged = !(old == null && old == input || old != null
						&& old.equals(input));
			}
		};
	}

	@Override
	public Collection<Object> getOutputIds() {
		return Arrays.asList((Object) RESULT);
	}

	@SuppressWarnings("unchecked")
	@Override
	public OutputGetter<Number> getOutput(Object id) {
		if (id.equals(RESULT)) {
			return new OutputGetter<Number>() {

				@Override
				public boolean isSet() {
					return output != null;
				}

				@Override
				public Number get() {
					return output;
				}
			};
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public boolean shouldBeExecuted() {
		return areInputsChanged;
	}

	@Override
	public boolean isExecutable() {
		return !inputs.values().contains(null);
	}

	@Override
	public void execute() {
		double value = 0;
		for (Number input : inputs.values()) {
			value += input.doubleValue();
		}
		output = value;
		areInputsChanged = false;
	}

}
