package fr.vergne.benchmark.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import fr.vergne.benchmark.Benchmark;
import fr.vergne.benchmark.Task;
import fr.vergne.benchmark.Link;

public class ImmutableBenchmark implements Benchmark {

	private final Collection<Task> tasks;
	private final Collection<Link<?>> links;

	public ImmutableBenchmark(Collection<Task> tasks,
			Collection<Link<?>> links) {
		this.tasks = Collections
				.unmodifiableCollection(new LinkedList<Task>(tasks));
		this.links = Collections
				.unmodifiableCollection(new LinkedList<Link<?>>(links));
	}
	
	public ImmutableBenchmark(Benchmark benchmark) {
		this(benchmark.getTasks(), benchmark.getLinks());
	}

	@Override
	public Collection<Task> getTasks() {
		return tasks;
	}

	@Override
	public Collection<Link<?>> getLinks() {
		return links;
	}

}
