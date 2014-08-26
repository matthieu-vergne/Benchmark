package fr.vergne.benchmark.testMaterial;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.OutputGetter;
import fr.vergne.benchmark.Task;

public class LinearTask implements Task {

	public static final String IN_OUT_ID = "value";
	private Object input = null;
	private Object output = null;
	private final InputSetter<Object> inputSetter = new InputSetter<Object>() {

		@Override
		public void set(Object input) {
			LinearTask.this.input = input;
		}
	};
	private final OutputGetter<Object> outputGetter = new OutputGetter<Object>() {

		@Override
		public boolean isSet() {
			return output != null;
		}

		@Override
		public Object get() {
			return output;
		}
	};

	@Override
	public boolean shouldBeExecuted() {
		return input != output;
	}

	@Override
	public boolean isExecutable() {
		return true;
	}

	@Override
	public Collection<Object> getOutputIds() {
		return Arrays.asList((Object) IN_OUT_ID);
	}

	@Override
	public OutputGetter<Object> getOutput(Object id) {
		if (id == IN_OUT_ID) {
			return outputGetter;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public Collection<Object> getInputIds() {
		return Arrays.asList((Object) IN_OUT_ID);
	}

	@Override
	public InputSetter<Object> getInput(Object id) {
		if (id == IN_OUT_ID) {
			return inputSetter;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void execute() {
		output = input;
	}
}
