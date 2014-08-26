package fr.vergne.benchmark;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.benchmark.testMaterial.LinearTask;

public class BenchmarkBuilderTest {

	@Test
	public void testClear() {
		BenchmarkBuilder builder = new BenchmarkBuilder();

		{
			Benchmark benchmark = builder.clear().createInstance();
			assertNotNull(benchmark.getTasks());
			assertEquals(0, benchmark.getTasks().size());
			assertNotNull(benchmark.getLinks());
			assertEquals(0, benchmark.getLinks().size());
		}

		{
			LinearTask t1 = new LinearTask();
			LinearTask t2 = new LinearTask();
			LinearTask t3 = new LinearTask();
			builder.add(t1).add(t2).add(t3);
			builder.link(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
			builder.link(t2, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
			Benchmark benchmark = builder.clear().createInstance();
			assertNotNull(benchmark.getTasks());
			assertEquals(0, benchmark.getTasks().size());
			assertNotNull(benchmark.getLinks());
			assertEquals(0, benchmark.getLinks().size());
		}
	}

	@Test
	public void testAddRemoveTask() {
		BenchmarkBuilder builder = new BenchmarkBuilder();

		builder.clear();
		assertEquals(0, builder.createInstance().getTasks().size());

		LinearTask t1 = new LinearTask();
		builder.add(t1);
		assertEquals(1, builder.createInstance().getTasks().size());

		LinearTask t2 = new LinearTask();
		builder.add(t2);
		assertEquals(2, builder.createInstance().getTasks().size());

		builder.remove(t1);
		assertEquals(1, builder.createInstance().getTasks().size());

		builder.remove(t2);
		assertEquals(0, builder.createInstance().getTasks().size());
	}

	@Test
	public void testLinkUnlink() {
		LinearTask t1 = new LinearTask();
		LinearTask t2 = new LinearTask();
		LinearTask t3 = new LinearTask();

		BenchmarkBuilder builder = new BenchmarkBuilder();
		builder.clear();
		builder.add(t1);
		builder.add(t2);
		builder.add(t3);

		assertEquals(0, builder.createInstance().getLinks().size());

		builder.link(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		assertEquals(1, builder.createInstance().getLinks().size());

		builder.link(t2, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		assertEquals(2, builder.createInstance().getLinks().size());

		builder.link(t1, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		assertEquals(3, builder.createInstance().getLinks().size());

		builder.link(t2, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		assertEquals(3, builder.createInstance().getLinks().size());

		builder.unlink(t3, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		assertEquals(3, builder.createInstance().getLinks().size());

		builder.unlink(t2, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		assertEquals(2, builder.createInstance().getLinks().size());

		builder.unlink(t1, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		assertEquals(1, builder.createInstance().getLinks().size());

		builder.unlink(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		assertEquals(0, builder.createInstance().getLinks().size());

		builder.unlink(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		assertEquals(0, builder.createInstance().getLinks().size());
	}

	@Test
	public void testRemoveException() {
		LinearTask t1 = new LinearTask();
		LinearTask t2 = new LinearTask();

		BenchmarkBuilder builder = new BenchmarkBuilder();
		builder.clear();
		builder.add(t1);
		builder.add(t2);

		builder.link(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		try {
			builder.remove(t1);
			fail("The task should not be removable.");
		} catch (IllegalArgumentException e) {
		}
		try {
			builder.remove(t2);
			fail("The task should not be removable.");
		} catch (IllegalArgumentException e) {
		}

		builder.unlink(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		builder.remove(t1);
		builder.remove(t2);
	}

	@Test
	public void testLinkException() {
		LinearTask t1 = new LinearTask();
		LinearTask t2 = new LinearTask();

		BenchmarkBuilder builder = new BenchmarkBuilder();
		builder.clear();

		try {
			builder.link(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
			fail("The link should not be possible.");
		} catch (IllegalArgumentException e) {
		}

		builder.add(t1);
		try {
			builder.link(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
			fail("The link should not be possible.");
		} catch (IllegalArgumentException e) {
		}

		builder.add(t2);
		builder.link(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
	}

	@Test
	public void testUnlinkAll() {
		LinearTask t1 = new LinearTask();
		LinearTask t2 = new LinearTask();
		LinearTask t3 = new LinearTask();

		BenchmarkBuilder builder = new BenchmarkBuilder();
		builder.clear();
		builder.add(t1);
		builder.add(t2);
		builder.add(t3);
		builder.link(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		builder.link(t2, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		builder.link(t1, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);

		assertEquals(3, builder.createInstance().getLinks().size());
		builder.unlinkAll(t2);
		assertEquals(1, builder.createInstance().getLinks().size());
		builder.unlink(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		assertEquals(1, builder.createInstance().getLinks().size());
		builder.unlink(t2, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		assertEquals(1, builder.createInstance().getLinks().size());
		builder.unlink(t1, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		assertEquals(0, builder.createInstance().getLinks().size());
	}

	@Test
	public void testClean() {
		LinearTask t1 = new LinearTask();
		LinearTask t2 = new LinearTask();
		LinearTask t3 = new LinearTask();

		BenchmarkBuilder builder = new BenchmarkBuilder();
		builder.clear();
		builder.add(t1);
		builder.add(t2);
		builder.add(t3);
		builder.link(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		builder.link(t2, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		builder.link(t1, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);

		assertTrue(builder.createInstance().getTasks().contains(t1));
		assertTrue(builder.createInstance().getTasks().contains(t2));
		assertTrue(builder.createInstance().getTasks().contains(t3));

		builder.clean();
		assertTrue(builder.createInstance().getTasks().contains(t1));
		assertTrue(builder.createInstance().getTasks().contains(t2));
		assertTrue(builder.createInstance().getTasks().contains(t3));

		builder.unlink(t1, LinearTask.IN_OUT_ID, t2, LinearTask.IN_OUT_ID);
		builder.clean();
		assertTrue(builder.createInstance().getTasks().contains(t1));
		assertTrue(builder.createInstance().getTasks().contains(t2));
		assertTrue(builder.createInstance().getTasks().contains(t3));

		builder.unlink(t2, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		builder.clean();
		assertTrue(builder.createInstance().getTasks().contains(t1));
		assertFalse(builder.createInstance().getTasks().contains(t2));
		assertTrue(builder.createInstance().getTasks().contains(t3));

		builder.unlink(t1, LinearTask.IN_OUT_ID, t3, LinearTask.IN_OUT_ID);
		builder.clean();
		assertFalse(builder.createInstance().getTasks().contains(t1));
		assertFalse(builder.createInstance().getTasks().contains(t2));
		assertFalse(builder.createInstance().getTasks().contains(t3));
	}

}
