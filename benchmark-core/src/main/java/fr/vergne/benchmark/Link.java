package fr.vergne.benchmark;

/**
 * A {@link Link} aims at setting the input of a {@link Task} based on another
 * {@link Task}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public interface Link<Type> {

	public Task getSourceTask();

	public Task getTargetTask();

	public Object getTargetId();

	public Type getValue();

	boolean isTransferable();

	public void transfer();

}
