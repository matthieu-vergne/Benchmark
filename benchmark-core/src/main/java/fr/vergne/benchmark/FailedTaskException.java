package fr.vergne.benchmark;

@SuppressWarnings("serial")
public class FailedTaskException extends RuntimeException {

	public FailedTaskException(Task task, Exception ex) {
		super("Execution failed for " + task, ex);
	}

}
