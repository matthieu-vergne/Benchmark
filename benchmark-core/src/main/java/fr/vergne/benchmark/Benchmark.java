package fr.vergne.benchmark;

import java.util.Collection;

public interface Benchmark {

	public Collection<Task> getTasks();

	public Collection<Link<?>> getLinks();

	public Collection<Object> getInputIds();

	public <Input> void setInput(Object id, Input value);

	public Collection<Object> getOutputIds();

	public <Output> Output getOutput(Object id);
}
