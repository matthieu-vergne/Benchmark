package fr.vergne.benchmark;

/**
 * An {@link InputSetter} is an accessor to set the input of a {@link Task}.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Input>
 */
public interface InputSetter<Input> {

	public void set(Input input);
}
