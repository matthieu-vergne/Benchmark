package fr.vergne.benchmark.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import fr.vergne.benchmark.Benchmark;
import fr.vergne.benchmark.Link;
import fr.vergne.benchmark.Task;
import fr.vergne.benchmark.util.IOIdentifier;

public class ImmutableBenchmark implements Benchmark {

	private final Collection<Task> tasks;
	private final Collection<Link<?>> links;
	private final Map<Object, IOIdentifier> inputs;
	private final Map<Object, IOIdentifier> outputs;

	public ImmutableBenchmark(Collection<Task> tasks,
			Collection<Link<?>> links, Map<Object, IOIdentifier> inputs,
			Map<Object, IOIdentifier> outputs) {
		this.tasks = Collections.unmodifiableCollection(new LinkedList<Task>(
				tasks));
		this.links = Collections
				.unmodifiableCollection(new LinkedList<Link<?>>(links));
		this.inputs = Collections
				.unmodifiableMap(new HashMap<Object, IOIdentifier>(inputs));
		this.outputs = Collections
				.unmodifiableMap(new HashMap<Object, IOIdentifier>(outputs));
	}

	@Override
	public Collection<Task> getTasks() {
		return tasks;
	}

	@Override
	public Collection<Link<?>> getLinks() {
		return links;
	}

	@Override
	public Collection<Object> getInputIds() {
		return inputs.keySet();
	}

	@Override
	public <Input> void setInput(Object id, Input value) {
		IOIdentifier ioIdentifier = inputs.get(id);
		ioIdentifier.getTask().getInput(ioIdentifier.getId()).set(value);
	}

	@Override
	public Collection<Object> getOutputIds() {
		return outputs.keySet();
	}

	@Override
	public <Output> Output getOutput(Object id) {
		IOIdentifier ioIdentifier = outputs.get(id);
		return ioIdentifier.getTask().<Output> getOutput(ioIdentifier.getId())
				.get();
	}
}
