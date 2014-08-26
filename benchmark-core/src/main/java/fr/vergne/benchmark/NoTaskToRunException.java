package fr.vergne.benchmark;

@SuppressWarnings("serial")
public class NoTaskToRunException extends Exception {

	public NoTaskToRunException(Benchmark benchmark) {
		super("No task to run for the benchmark " + benchmark);
	}
}
