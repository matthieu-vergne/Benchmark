package fr.vergne.benchmark.event.impl;

import java.util.logging.Logger;

import fr.vergne.benchmark.Link;
import fr.vergne.benchmark.Task;
import fr.vergne.benchmark.event.BenchmarkEvent;
import fr.vergne.benchmark.event.BenchmarkEventListener;
import fr.vergne.benchmark.event.LinkTransferedEvent;
import fr.vergne.benchmark.event.TaskExecutedEvent;
import fr.vergne.benchmark.event.TaskFailedEvent;
import fr.vergne.benchmark.event.TaskSelectedEvent;
import fr.vergne.logging.LoggerConfiguration;

/**
 * This {@link LogListener} is a {@link BenchmarkEventListener} which logs all
 * the {@link BenchmarkEvent}s generated.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
// FIXME test
public class LogListener implements BenchmarkEventListener {

	public final Logger logger;

	/**
	 * Creates a {@link LogListener} with a default {@link Logger}.
	 */
	public LogListener() {
		logger = LoggerConfiguration.getSimpleLogger();
	}

	/**
	 * Creates a {@link LogListener} with the specified {@link Logger}.
	 */
	public LogListener(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void eventGenerated(BenchmarkEvent event) {
		if (event instanceof TaskSelectedEvent) {
			Task task = ((TaskSelectedEvent) event).getTask();
			logger.info("Task selected: " + task);
		} else if (event instanceof TaskExecutedEvent) {
			Task task = ((TaskExecutedEvent) event).getTask();
			logger.info("Task executed: " + task);
		} else if (event instanceof TaskFailedEvent) {
			Task task = ((TaskFailedEvent) event).getTask();
			Exception cause = ((TaskFailedEvent) event).getCause();
			logger.warning("Task executed but failed: " + task);
			logger.warning("Failure message: " + cause.getMessage());
		} else if (event instanceof LinkTransferedEvent) {
			Link<?> link = ((LinkTransferedEvent) event).getLink();
			logger.info("Link activated: " + link + " with value "
					+ link.getValue());
		} else {
			logger.warning("Unmanaged event: " + event);
		}
	}
}
