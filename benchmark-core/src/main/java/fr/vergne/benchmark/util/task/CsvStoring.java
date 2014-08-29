package fr.vergne.benchmark.util.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import fr.vergne.benchmark.InputSetter;
import fr.vergne.benchmark.OutputGetter;
import fr.vergne.benchmark.impl.AbstractSimpleTask;

/**
 * A {@link CsvStoring} receive some inputs which will be stores in a CSV file
 * when it will be executed. When instantiating a {@link CsvStoring}, the
 * {@link Renderer}s should be provided, but if the values provided have their
 * own {@link String} representation <code>null</code> can be provided as a
 * {@link Renderer}. A simple {@link Renderer} will be automatically assigned.
 * If it applies to all inputs, one can simply provide the IDs of the inputs.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
// FIXME test
public class CsvStoring extends AbstractSimpleTask {

	public static final Object CSV = "csv";
	public static final Object RESET = "reset";
	public static final Object ROW_COUNTER = "row counter";
	private PrintStream csv = null;
	private boolean headerPrinted = false;
	private long rowcounter = 0;
	private final Map<String, Object> recordedInputs;
	private final Map<String, Renderer> renderers;
	private final String separator;
	private static final Renderer DEFAULT_RENDERER = new Renderer() {

		@Override
		public String render(Object object) {
			return object == null ? "null" : object.toString();
		}
	};

	/**
	 * Creates a {@link CsvStoring} with specific {@link Renderer}s for each
	 * input. If the ones having no {@link Renderer} (<code>null</code>), a
	 * default one will be provided, which relies on the
	 * {@link Object#toString()} method.
	 * 
	 * @param inputsAndRenderers
	 *            the input IDs and the corresponding {@link Renderer}s
	 * @param separator
	 *            the separator character to use in the CSV file
	 */
	public CsvStoring(Map<String, Renderer> inputsAndRenderers, String separator) {
		Collection<Object> invalidIds = Arrays.asList(CSV, RESET, ROW_COUNTER);
		if (!Collections.disjoint(invalidIds, inputsAndRenderers.keySet())) {
			throw new IllegalArgumentException("You cannot use these IDs: "
					+ invalidIds);
		} else {
			// no overlapping with default IDs
		}

		recordedInputs = new LinkedHashMap<String, Object>();
		for (String id : inputsAndRenderers.keySet()) {
			recordedInputs.put(id, null);
		}
		renderers = new LinkedHashMap<String, Renderer>();
		for (Entry<String, Renderer> entry : inputsAndRenderers.entrySet()) {
			String id = entry.getKey();
			Renderer renderer = entry.getValue();
			renderer = renderer == null ? DEFAULT_RENDERER : renderer;
			renderers.put(id, renderer);
		}
		this.separator = separator;
	}

	/**
	 * Equivalent to {@link #CsvStoring(Map, String)} with the separator at ",".
	 */
	public CsvStoring(Map<String, Renderer> inputsAndRenderers) {
		this(inputsAndRenderers, ",");
	}

	/**
	 * Create a {@link CsvStoring} with a set of inputs which all have their own
	 * {@link String} representation already defined through their
	 * {@link Object#toString()} method. This is a convenient method when
	 * defining {@link Renderer}s is not needed.
	 * 
	 * @param inputIds
	 *            the IDs of the inputs to manage
	 * @param separator
	 *            the separator character to use in the CSV file
	 */
	public CsvStoring(Collection<String> inputIds, String separator) {
		this(buildRendererMap(inputIds), separator);
	}

	/**
	 * Equivalent to {@link #CsvStoring(Collection, String)} with the separator
	 * at ",".
	 */
	public CsvStoring(Collection<String> inputIds) {
		this(inputIds, ",");
	}

	private static Map<String, Renderer> buildRendererMap(
			Collection<String> inputIds) {
		Map<String, Renderer> map = new LinkedHashMap<String, CsvStoring.Renderer>();
		for (String id : inputIds) {
			map.put(id, null);
		}
		return map;
	}

	@Override
	protected Map<Object, InputSetter<?>> getInputs() {
		Map<Object, InputSetter<?>> inputs = new HashMap<Object, InputSetter<?>>();
		inputs.put(CSV, new InputSetter<File>() {

			@Override
			public void set(File input) {
				if (input.exists()) {
					String content;
					try {
						content = FileUtils.readFileToString(input);
					} catch (IOException e) {
						throw new RuntimeException(
								"Impossible to read the file " + input, e);
					}
					String fixedContent = content.replaceAll("[^\\n]+$", "");
					if (fixedContent.equals(content)) {
						// nothing to fix, keep the original
					} else {
						try {
							FileUtils.write(input, content);
						} catch (IOException e) {
							throw new RuntimeException(
									"Impossible to fix the file " + input, e);
						}
					}
					String newlines = fixedContent.replaceAll("[^\\n]+", "");
					int rows = newlines.length();
					headerPrinted = rows >= 1;
					rowcounter = rows - 1;
				} else {
					headerPrinted = false;
					rowcounter = 0;
				}
				try {
					csv = new PrintStream(new FileOutputStream(input, true));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					csv = null;
				}
			}
		});
		for (final String id : recordedInputs.keySet()) {
			inputs.put(id, new InputSetter<Object>() {

				@Override
				public void set(Object input) {
					recordedInputs.put(id, input);
				}
			});
		}
		return inputs;
	}

	@Override
	protected Object getResetInputId() {
		return RESET;
	}

	@Override
	protected Map<Object, OutputGetter<?>> getOutputs() {
		Map<Object, OutputGetter<?>> outputs = new HashMap<Object, OutputGetter<?>>();
		outputs.put(ROW_COUNTER, new OutputGetter<Long>() {

			@Override
			public Long get() {
				return rowcounter;
			}

			@Override
			public boolean isSet() {
				return true;
			}
		});
		return outputs;
	}

	@Override
	protected void doExecute() {
		if (!headerPrinted) {
			csv.println(StringUtils.join(recordedInputs.keySet(), separator));
			headerPrinted = true;
		} else {
			// header already printed
		}
		Collection<String> values = new LinkedList<String>();
		for (String id : recordedInputs.keySet()) {
			Object object = recordedInputs.get(id);
			String value = renderers.get(id).render(object);
			values.add(value);
		}
		csv.println(StringUtils.join(values, separator));
		rowcounter++;
	}

	/**
	 * A {@link Renderer} aims at providing a {@link String} representation to
	 * an {@link Object}.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 */
	public static interface Renderer {
		public String render(Object object);
	}
}
