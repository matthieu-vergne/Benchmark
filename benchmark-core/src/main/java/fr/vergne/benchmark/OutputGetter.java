package fr.vergne.benchmark;

/**
 * An {@link OutputSetter} is an accessor to get the output of a {@link Task}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Output>
 */
public interface OutputGetter<Output> {

	public Output get();

	/**
	 * 
	 * @return <code>true</code> if {@link #get()} can be called safely (to
	 *         obtain an exploitable value).
	 */
	public boolean isSet();
}
