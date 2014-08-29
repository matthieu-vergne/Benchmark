package fr.vergne.benchmark.util.task;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import fr.vergne.benchmark.Benchmark;
import fr.vergne.benchmark.BenchmarkBuilder;
import fr.vergne.benchmark.BenchmarkRunner;
import fr.vergne.benchmark.NoTaskToRunException;
import fr.vergne.benchmark.Task;

public class MapBrowserTest {

	@Test
	public void testItemRetrieval() throws NoTaskToRunException {
		Task task = new MapBrowser<Integer, String>();
		Benchmark benchmark = new BenchmarkBuilder().add(task).createInstance();
		BenchmarkRunner runner = new BenchmarkRunner();
		runner.setBenchmark(benchmark);

		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "abc");
		map.put(2, "ABC");
		map.put(3, "test");
		map.put(4, "TEST");
		map.put(5, "");

		task.getInput(MapBrowser.MAP).set(map);
		Random rand = new Random();
		for (int i = 0; i < 1000; i++) {
			int index = rand.nextInt(5) + 1;
			task.getInput(MapBrowser.RESET)
					.set(Arrays.asList(MapBrowser.INDEX));
			task.getInput(MapBrowser.INDEX).set(index);
			runner.run();
			String selected = (String) task.getOutput(MapBrowser.SELECTION)
					.get();
			assertEquals("For index " + index, map.get(index), selected);
		}
	}

}
