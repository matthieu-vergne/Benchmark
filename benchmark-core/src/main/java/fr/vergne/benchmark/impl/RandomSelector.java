package fr.vergne.benchmark.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.OutputGetter;
import fr.vergne.benchmark.Task;

/**
 * A {@link RandomSelector} is a {@link Task} aiming at selecting a random item
 * among a collection.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Item>
 */
public class RandomSelector<Item> extends AbstractSimpleTask {

	public static final String COLLECTION = "collection";
	public static final String SELECTION = "selection";
	public static final String RESET = "reset";
	private final Random rand = new Random();
	private List<Item> items = null;
	private Item selected = null;

	@Override
	protected Map<Object, InputSetter<?>> getInputs() {
		Map<Object, InputSetter<?>> map = new HashMap<Object, InputSetter<?>>();
		map.put(COLLECTION, new InputSetter<Collection<Item>>() {

			@Override
			public void set(Collection<Item> input) {
				items = new LinkedList<Item>(input);
			}
		});
		return map;
	}

	@Override
	protected Map<Object, OutputGetter<?>> getOutputs() {
		Map<Object, OutputGetter<?>> map = new HashMap<Object, OutputGetter<?>>();
		map.put(SELECTION, new OutputGetter<Item>() {

			@Override
			public Item get() {
				return selected;
			}

			@Override
			public boolean isSet() {
				return !shouldBeExecuted();
			}
		});
		return map;
	}

	@Override
	protected Object getResetInputId() {
		return RESET;
	}

	@Override
	protected void doExecute() {
		selected = items.get(rand.nextInt(items.size()));
	}

}
