package fr.vergne.benchmark.event;

import fr.vergne.benchmark.Link;

public class LinkTransferedEvent implements BenchmarkEvent {

	private Link<?> link;

	public LinkTransferedEvent(Link<?> link) {
		this.link = link;
	}

	public Link<?> getLink() {
		return link;
	}

}
