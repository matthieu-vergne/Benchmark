package fr.vergne.benchmark;

import java.util.Collection;

public interface Benchmark {

	public Collection<Task> getTasks();

	public Collection<Link<?>> getLinks();

}
