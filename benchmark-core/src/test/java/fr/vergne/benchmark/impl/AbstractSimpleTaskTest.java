package fr.vergne.benchmark.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import fr.vergne.benchmark.Benchmark;
import fr.vergne.benchmark.BenchmarkBuilder;
import fr.vergne.benchmark.BenchmarkRunner;
import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.NoTaskToRunException;
import fr.vergne.benchmark.OutputGetter;

public class AbstractSimpleTaskTest {

	@Test
	public void testInputOutputResetDefinition() {
		SimpleTask task = new SimpleTask(null);

		assertEquals(3, task.getInputIds().size());
		assertTrue(task.getInputIds().contains(SimpleTask.INPUT_1));
		assertTrue(task.getInputIds().contains(SimpleTask.INPUT_2));
		assertTrue(task.getInputIds().contains(SimpleTask.INPUT_3));

		assertEquals(2, task.getOutputIds().size());
		assertTrue(task.getOutputIds().contains(SimpleTask.OUTPUT_1));
		assertTrue(task.getOutputIds().contains(SimpleTask.OUTPUT_2));

		Object resetId = "reset";
		SimpleTask taskWithReset = new SimpleTask(resetId);

		assertEquals(4, taskWithReset.getInputIds().size());
		assertTrue(taskWithReset.getInputIds().contains(SimpleTask.INPUT_1));
		assertTrue(taskWithReset.getInputIds().contains(SimpleTask.INPUT_2));
		assertTrue(taskWithReset.getInputIds().contains(SimpleTask.INPUT_3));
		assertTrue(taskWithReset.getInputIds().contains(resetId));

		assertEquals(2, taskWithReset.getOutputIds().size());
		assertTrue(taskWithReset.getOutputIds().contains(SimpleTask.OUTPUT_1));
		assertTrue(taskWithReset.getOutputIds().contains(SimpleTask.OUTPUT_2));
	}

	@Test
	public void testExecutionWhenAllInputsSet() {
		SimpleTask task = new SimpleTask(null);

		Benchmark benchmark = new BenchmarkBuilder().add(task).createInstance();
		BenchmarkRunner runner = new BenchmarkRunner();
		runner.setBenchmark(benchmark);

		try {
			runner.run();
			fail("No exception thrown");
		} catch (NoTaskToRunException e) {
		}

		task.getInput(SimpleTask.INPUT_1).set(1);
		try {
			runner.run();
			fail("No exception thrown");
		} catch (NoTaskToRunException e) {
		}

		task.getInput(SimpleTask.INPUT_2).set(2);
		try {
			runner.run();
			fail("No exception thrown");
		} catch (NoTaskToRunException e) {
		}

		task.getInput(SimpleTask.INPUT_3).set(3);
		try {
			runner.run();
		} catch (NoTaskToRunException e) {
			fail("Task non executed when all inputs set");
		}
	}

	@Test
	public void testExecutionsWithResets() {
		Object resetId = "reset";
		SimpleTask task = new SimpleTask(resetId);

		Benchmark benchmark = new BenchmarkBuilder().add(task).createInstance();
		BenchmarkRunner runner = new BenchmarkRunner();
		runner.setBenchmark(benchmark);

		task.getInput(SimpleTask.INPUT_1).set(1);
		task.getInput(SimpleTask.INPUT_2).set(2);
		task.getInput(SimpleTask.INPUT_3).set(3);
		try {
			runner.run();
		} catch (NoTaskToRunException e) {
			fail("Task non executed when all inputs set");
		}

		try {
			runner.run();
			fail("No exception thrown when already executed");
		} catch (NoTaskToRunException e) {
		}

		task.getInput(resetId).set(Arrays.asList());
		try {
			runner.run();
		} catch (NoTaskToRunException e) {
			fail("Task non executed when all inputs set");
		}

		try {
			runner.run();
			fail("No exception thrown when already executed");
		} catch (NoTaskToRunException e) {
		}

		task.getInput(resetId).set(
				Arrays.asList(SimpleTask.INPUT_2, SimpleTask.INPUT_3));
		try {
			runner.run();
			fail("No exception thrown when inputs reset");
		} catch (NoTaskToRunException e) {
		}

		task.getInput(SimpleTask.INPUT_2).set(2);
		try {
			runner.run();
			fail("No exception thrown when inputs reset");
		} catch (NoTaskToRunException e) {
		}

		task.getInput(SimpleTask.INPUT_3).set(3);
		try {
			runner.run();
		} catch (NoTaskToRunException e) {
			fail("Task non executed when all inputs set");
		}
	}

	private static class SimpleTask extends AbstractSimpleTask {

		public static final Object INPUT_1 = "input1";
		public static final Object INPUT_2 = "input2";
		public static final Object INPUT_3 = "input3";
		public static final Object OUTPUT_1 = "output1";
		public static final Object OUTPUT_2 = "output2";
		private int i1 = 0;
		private int i2 = 0;
		private int i3 = 0;
		private Integer o1 = null;
		private Integer o2 = null;
		private final Object resetId;

		public SimpleTask(Object resetId) {
			this.resetId = resetId;
		}

		@Override
		protected Object getResetInputId() {
			return resetId;
		}

		@Override
		protected Map<Object, InputSetter<?>> getInputs() {
			Map<Object, InputSetter<?>> inputs = new HashMap<Object, InputSetter<?>>();
			inputs.put(INPUT_1, new InputSetter<Integer>() {

				@Override
				public void set(Integer input) {
					i1 = input;
				}
			});
			inputs.put(INPUT_2, new InputSetter<Integer>() {

				@Override
				public void set(Integer input) {
					i2 = input;
				}
			});
			inputs.put(INPUT_3, new InputSetter<Integer>() {

				@Override
				public void set(Integer input) {
					i3 = input;
				}
			});
			return inputs;
		}

		@Override
		protected Map<Object, OutputGetter<?>> getOutputs() {
			Map<Object, OutputGetter<?>> outputs = new HashMap<Object, OutputGetter<?>>();
			outputs.put(OUTPUT_1, new OutputGetter<Integer>() {

				@Override
				public Integer get() {
					return o1;
				}

				@Override
				public boolean isSet() {
					return o1 != null;
				}
			});
			outputs.put(OUTPUT_2, new OutputGetter<Integer>() {

				@Override
				public Integer get() {
					return o2;
				}

				@Override
				public boolean isSet() {
					return o2 != null;
				}
			});
			return outputs;
		}

		@Override
		protected void doExecute() {
			o1 = i1 + i2;
			o2 = i2 * i3;
		}
	}
}
