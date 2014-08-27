package fr.vergne.benchmark;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.benchmark.event.BenchmarkEvent;
import fr.vergne.benchmark.event.BenchmarkEventListener;
import fr.vergne.benchmark.event.LinkTransferedEvent;
import fr.vergne.benchmark.event.TaskExecutedEvent;
import fr.vergne.benchmark.event.TaskFailedEvent;
import fr.vergne.benchmark.event.TaskSelectedEvent;
import fr.vergne.benchmark.testMaterial.AdditionTask;
import fr.vergne.benchmark.testMaterial.LinearTask;

public class BenchmarkRunnerTest {

	@Test
	public void testLinearBenchmark() throws NoTaskToRunException {
		LinearTask t1 = new LinearTask();
		LinearTask t2 = new LinearTask();
		LinearTask t3 = new LinearTask();
		LinearTask t4 = new LinearTask();
		LinearTask t5 = new LinearTask();
		String id = LinearTask.IN_OUT_ID;

		BenchmarkBuilder builder = new BenchmarkBuilder();
		builder.addAll(t1, t2, t3, t4, t5);
		builder.link(t1, id, t2, id);
		builder.link(t2, id, t3, id);
		builder.link(t3, id, t4, id);
		builder.link(t4, id, t5, id);

		BenchmarkRunner runner = new BenchmarkRunner();
		runner.setBenchmark(builder.createInstance());
		assertNull(t1.getOutput(id).get());
		assertNull(t2.getOutput(id).get());
		assertNull(t3.getOutput(id).get());
		assertNull(t4.getOutput(id).get());
		assertNull(t5.getOutput(id).get());

		t1.getInput(id).set(1);
		assertNull(t1.getOutput(id).get());
		assertNull(t2.getOutput(id).get());
		assertNull(t3.getOutput(id).get());
		assertNull(t4.getOutput(id).get());
		assertNull(t5.getOutput(id).get());

		runner.run();
		assertEquals(1, t1.getOutput(id).get());
		assertNull(t2.getOutput(id).get());
		assertNull(t3.getOutput(id).get());
		assertNull(t4.getOutput(id).get());
		assertNull(t5.getOutput(id).get());

		runner.run();
		assertEquals(1, t1.getOutput(id).get());
		assertEquals(1, t2.getOutput(id).get());
		assertNull(t3.getOutput(id).get());
		assertNull(t4.getOutput(id).get());
		assertNull(t5.getOutput(id).get());

		runner.run();
		assertEquals(1, t1.getOutput(id).get());
		assertEquals(1, t2.getOutput(id).get());
		assertEquals(1, t3.getOutput(id).get());
		assertNull(t4.getOutput(id).get());
		assertNull(t5.getOutput(id).get());

		runner.run();
		assertEquals(1, t1.getOutput(id).get());
		assertEquals(1, t2.getOutput(id).get());
		assertEquals(1, t3.getOutput(id).get());
		assertEquals(1, t4.getOutput(id).get());
		assertNull(t5.getOutput(id).get());

		runner.run();
		assertEquals(1, t1.getOutput(id).get());
		assertEquals(1, t2.getOutput(id).get());
		assertEquals(1, t3.getOutput(id).get());
		assertEquals(1, t4.getOutput(id).get());
		assertEquals(1, t5.getOutput(id).get());

		try {
			runner.run();
			fail("No task should be run.");
		} catch (NoTaskToRunException e) {
		}
	}

	@Test
	public void testLayersBenchmark() throws NoTaskToRunException {
		AdditionTask a1 = new AdditionTask();
		AdditionTask a2 = new AdditionTask();
		AdditionTask a3 = new AdditionTask();
		AdditionTask b1 = new AdditionTask();
		AdditionTask b2 = new AdditionTask();
		AdditionTask b3 = new AdditionTask();
		AdditionTask c1 = new AdditionTask();
		AdditionTask c2 = new AdditionTask();
		AdditionTask c3 = new AdditionTask();

		BenchmarkBuilder builder = new BenchmarkBuilder();
		builder.addAll(a1, a2, a3, b1, b2, b3, c1, c2, c3);
		String out = AdditionTask.RESULT;
		builder.link(a1, out, b1, 1);
		builder.link(a1, out, b2, 1);
		builder.link(a1, out, b3, 1);
		builder.link(a2, out, b1, 2);
		builder.link(a2, out, b2, 2);
		builder.link(a3, out, b3, 2);
		builder.link(b1, out, c1, 1);
		builder.link(b1, out, c2, 1);
		builder.link(b1, out, c3, 1);
		builder.link(b2, out, c1, 2);
		builder.link(b2, out, c2, 2);
		builder.link(b2, out, c3, 2);
		builder.link(b3, out, c1, 3);
		builder.link(b3, out, c2, 3);
		builder.link(b3, out, c3, 3);

		BenchmarkRunner runner = new BenchmarkRunner();
		runner.setBenchmark(builder.createInstance());
		assertNull(a1.getOutput(out).get());
		assertNull(a2.getOutput(out).get());
		assertNull(a3.getOutput(out).get());
		assertNull(b1.getOutput(out).get());
		assertNull(b2.getOutput(out).get());
		assertNull(b3.getOutput(out).get());
		assertNull(c1.getOutput(out).get());
		assertNull(c2.getOutput(out).get());
		assertNull(c3.getOutput(out).get());

		a1.getInput(1).set(1);
		a1.getInput(2).set(1);
		a2.getInput(1).set(5);
		a3.getInput(1).set(1);
		a3.getInput(2).set(1);
		a3.getInput(3).set(1);
		assertNull(a1.getOutput(out).get());
		assertNull(a2.getOutput(out).get());
		assertNull(a3.getOutput(out).get());
		assertNull(b1.getOutput(out).get());
		assertNull(b2.getOutput(out).get());
		assertNull(b3.getOutput(out).get());
		assertNull(c1.getOutput(out).get());
		assertNull(c2.getOutput(out).get());
		assertNull(c3.getOutput(out).get());

		runner.run();
		runner.run();
		runner.run();
		assertEquals(2, a1.getOutput(out).get().intValue());
		assertEquals(5, a2.getOutput(out).get().intValue());
		assertEquals(3, a3.getOutput(out).get().intValue());
		assertNull(b1.getOutput(out).get());
		assertNull(b2.getOutput(out).get());
		assertNull(b3.getOutput(out).get());
		assertNull(c1.getOutput(out).get());
		assertNull(c2.getOutput(out).get());
		assertNull(c3.getOutput(out).get());

		runner.run();
		runner.run();
		runner.run();
		assertEquals(2, a1.getOutput(out).get().intValue());
		assertEquals(5, a2.getOutput(out).get().intValue());
		assertEquals(3, a3.getOutput(out).get().intValue());
		assertEquals(7, b1.getOutput(out).get().intValue());
		assertEquals(7, b2.getOutput(out).get().intValue());
		assertEquals(5, b3.getOutput(out).get().intValue());
		assertNull(c1.getOutput(out).get());
		assertNull(c2.getOutput(out).get());
		assertNull(c3.getOutput(out).get());

		runner.run();
		runner.run();
		runner.run();
		assertEquals(2, a1.getOutput(out).get().intValue());
		assertEquals(5, a2.getOutput(out).get().intValue());
		assertEquals(3, a3.getOutput(out).get().intValue());
		assertEquals(7, b1.getOutput(out).get().intValue());
		assertEquals(7, b2.getOutput(out).get().intValue());
		assertEquals(5, b3.getOutput(out).get().intValue());
		assertEquals(19, c1.getOutput(out).get().intValue());
		assertEquals(19, c2.getOutput(out).get().intValue());
		assertEquals(19, c3.getOutput(out).get().intValue());

		try {
			runner.run();
			fail("No task should be run.");
		} catch (NoTaskToRunException e) {
		}
	}

	@Test
	public void testLinearEvents() throws NoTaskToRunException {
		final LinearTask t1 = new LinearTask();
		final LinearTask t2 = new LinearTask();
		final LinearTask t3 = new LinearTask() {
			@Override
			public void execute() {
				throw new RuntimeException();
			}
		};
		String id = LinearTask.IN_OUT_ID;

		BenchmarkBuilder builder = new BenchmarkBuilder();
		builder.addAll(t1, t2, t3);
		builder.link(t1, id, t2, id);
		builder.link(t2, id, t3, id);
		t1.getInput(id).set(1);

		BenchmarkRunner runner = new BenchmarkRunner();
		runner.setBenchmark(builder.createInstance());
		runner.registerListener(new BenchmarkEventListener() {

			private int step = 0;

			@Override
			public void eventGenerated(BenchmarkEvent event) {
				if (step == 0) {
					assertTrue(event instanceof TaskSelectedEvent);
					TaskSelectedEvent e = (TaskSelectedEvent) event;
					assertEquals(t1, e.getTask());
				} else if (step == 1) {
					assertTrue(event instanceof TaskExecutedEvent);
					TaskExecutedEvent e = (TaskExecutedEvent) event;
					assertEquals(t1, e.getTask());
				} else if (step == 2) {
					assertTrue(event instanceof LinkTransferedEvent);
					Link<?> link = ((LinkTransferedEvent) event).getLink();
					assertEquals(t1, link.getSourceTask());
					assertEquals(LinearTask.IN_OUT_ID, link.getSourceId());
					assertEquals(t2, link.getTargetTask());
					assertEquals(LinearTask.IN_OUT_ID, link.getTargetId());
				} else if (step == 3) {
					assertTrue(event instanceof TaskSelectedEvent);
					TaskSelectedEvent e = (TaskSelectedEvent) event;
					assertEquals(t2, e.getTask());
				} else if (step == 4) {
					assertTrue(event instanceof TaskExecutedEvent);
					TaskExecutedEvent e = (TaskExecutedEvent) event;
					assertEquals(t2, e.getTask());
				} else if (step == 5) {
					assertTrue(event instanceof LinkTransferedEvent);
					Link<?> link = ((LinkTransferedEvent) event).getLink();
					assertEquals(t2, link.getSourceTask());
					assertEquals(LinearTask.IN_OUT_ID, link.getSourceId());
					assertEquals(t3, link.getTargetTask());
					assertEquals(LinearTask.IN_OUT_ID, link.getTargetId());
				} else if (step == 6) {
					assertTrue(event instanceof TaskSelectedEvent);
					TaskSelectedEvent e = (TaskSelectedEvent) event;
					assertEquals(t3, e.getTask());
				} else if (step == 7) {
					assertTrue(event instanceof TaskFailedEvent);
					TaskFailedEvent e = (TaskFailedEvent) event;
					assertEquals(t3, e.getTask());
				} else {
					fail("Wrong step: " + step);
				}
				step++;
			}
		});
		runner.run();
		runner.run();
		try {
			runner.run();
			fail("No task should be run.");
		} catch (FailedTaskException e) {
		}
	}
}
