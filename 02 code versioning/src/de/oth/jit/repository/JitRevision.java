package de.oth.jit.repository;

import java.io.Serializable;

public final class JitRevision implements Serializable {
	private static final long serialVersionUID = -1309224658023722870L;
	
	private final String message, revision;
	
	public JitRevision(String message, String revision) {
		this.message = message;
		this.revision = revision;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getRevision() {
		return this.revision;
	}
	
	public JitRevision clone() {
		return new JitRevision(this.message, this.revision);
	}
}
