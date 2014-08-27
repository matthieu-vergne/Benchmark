package fr.vergne.benchmark.impl;

import java.util.HashMap;
import java.util.Map;

import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.OutputGetter;
import fr.vergne.benchmark.Task;

/**
 * A simple {@link Task} to retrieve an item from a {@link Map}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <K>
 * @param <V>
 */
public class MapBrowser<K, V> extends AbstractSimpleTask {

	public static final String MAP = "map";
	public static final String INDEX = "index";
	public static final String SELECTION = "selection";
	public static final String RESET = "reset";
	private Map<K, V> map = null;
	private K index = null;
	private V selected = null;

	@Override
	protected Map<Object, InputSetter<?>> getInputs() {
		Map<Object, InputSetter<?>> inputs = new HashMap<Object, InputSetter<?>>();
		inputs.put(MAP, new InputSetter<Map<K, V>>() {

			@Override
			public void set(Map<K, V> input) {
				map = input;
			}
		});
		inputs.put(INDEX, new InputSetter<K>() {

			@Override
			public void set(K input) {
				index = input;
			}
		});
		return inputs;
	}

	@Override
	protected Map<Object, OutputGetter<?>> getOutputs() {
		Map<Object, OutputGetter<?>> outputs = new HashMap<Object, OutputGetter<?>>();
		outputs.put(SELECTION, new OutputGetter<V>() {

			@Override
			public V get() {
				return selected;
			}

			@Override
			public boolean isSet() {
				return !shouldBeExecuted();
			}
		});
		return outputs;
	}

	@Override
	protected Object getResetInputId() {
		return RESET;
	}

	@Override
	protected void doExecute() {
		selected = map.get(index);
	}
}
