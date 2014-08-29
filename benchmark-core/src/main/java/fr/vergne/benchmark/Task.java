package fr.vergne.benchmark;

import java.util.Collection;

/**
 * A {@link Task} is a piece of the {@link Benchmark} which aims at
 * executing a single, atomic task. Each {@link Task} typically has
 * inputs (or parameters) and outputs (or results). These inputs/outputs can be
 * accessed to set or get there values. In particular, this is by this way that
 * the output of a {@link Task} can be linked to the input of another
 * in {@link Benchmark#linkOutput(Task, Object, Task, Object)}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface Task {

	/**
	 * 
	 * @return all the IDs of the inputs of this {@link Task}
	 */
	public Collection<Object> getInputIds();

	/**
	 * 
	 * @param id
	 *            the ID of the wanted input
	 * @return the accessor of the requested input
	 */
	public <In> InputSetter<In> getInput(Object id);

	/**
	 * 
	 * @return all the IDs of the outputs of this {@link Task}
	 */
	public Collection<Object> getOutputIds();

	/**
	 * 
	 * @param id
	 *            the ID of the wanted output
	 * @return the accessor of the requested output
	 */
	public <Out> OutputGetter<Out> getOutput(Object id);

	/**
	 * This method aims at identifying the {@link Task} which needs to
	 * be executed. Typically, all the {@link Task}s are requested to
	 * be executed from the start (<code>true</code>) and not anymore once
	 * executed (<code>false</code>) unless they need to be re-executed (in
	 * general after some others have been executed).
	 * 
	 * @return <code>true</code> if {@link #execute()} should be called,
	 *         <code>false</code> otherwise.
	 * @see #isExecutable()
	 */
	public boolean shouldBeExecuted();

	/**
	 * This method aims at avoiding the execution of this {@link Task}
	 * while it is not ready. Typically, the firsts to be executed in the
	 * {@link Benchmark} are executable from the start, while the others are
	 * executable only when their inputs have been set correctly.
	 * 
	 * @return <code>true</code> if {@link #execute()} can be called safely,
	 *         <code>false</code> otherwise
	 * @see #shouldBeExecuted()
	 */
	public boolean isExecutable();

	/**
	 * Execute this {@link Task}, allowing to produce the outputs
	 * corresponding to the current inputs. If no exception is thrown, the
	 * execution of this {@link Task} is assumed to be a success at the
	 * end of the execution of this method.<br/>
	 * <br/>
	 * Normally, this method should be called only if {@link #isExecutable()}
	 * and {@link #shouldBeExecuted()} return both <code>true</code>.
	 */
	public void execute();
}
