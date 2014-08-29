package fr.vergne.benchmark.util.task;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import fr.vergne.benchmark.Benchmark;
import fr.vergne.benchmark.BenchmarkBuilder;
import fr.vergne.benchmark.BenchmarkRunner;
import fr.vergne.benchmark.NoTaskToRunException;
import fr.vergne.benchmark.Task;

public class RandomSelectorTest {

	@Test
	public void testRandomSelection() throws NoTaskToRunException {
		Task task = new RandomSelector<Integer>();
		Benchmark benchmark = new BenchmarkBuilder().add(task).createInstance();
		BenchmarkRunner runner = new BenchmarkRunner();
		runner.setBenchmark(benchmark);

		List<Integer> collection = Arrays.asList(1, 2, 3, 4, 5);
		task.getInput(RandomSelector.COLLECTION).set(collection);

		Map<Integer, Integer> count = new HashMap<Integer, Integer>();
		for (int i = 0; i < 1000; i++) {
			runner.run();
			int selected = (Integer) task.getOutput(RandomSelector.SELECTION)
					.get();

			if (!count.containsKey(selected)) {
				count.put(selected, 1);
			} else {
				count.put(selected, count.get(selected) + 1);
			}
			task.getInput(RandomSelector.RESET).set(Collections.emptyList());
		}
		assertTrue(collection.containsAll(count.keySet()));

		// not mandatory but highly probable
		String optional = "(optional)";
		assertTrue(optional, count.keySet().containsAll(collection));
		for (Integer value : collection) {
			// average at 200
			assertTrue(optional, count.get(value) > 100);
			assertTrue(optional, count.get(value) < 300);
		}
	}

}
