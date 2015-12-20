package de.oth.jit.repository;

import java.io.Serializable;

/**
 * This wrapper class represents a committed revision. It contains the commit's
 * message and it's revision hash. 
 * 
 * @author Michael Neu
 */
public final class JitRevision implements Serializable {
	private static final long serialVersionUID = -1309224658023722870L;
	
	private final String message, revision;
	
	/**
	 * Initialize the wrapper with the commit message and the commit's revision
	 * hash. 
	 * 
	 * @param message  The message used to commit
	 * @param revision The resulting revision hash
	 */
	public JitRevision(String message, String revision) {
		this.message = message;
		this.revision = revision;
	}
	
	/**
	 * Get the commit's message. 
	 * 
	 * @return The message used to commit
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Get the commit's revision hash. 
	 * 
	 * @return The resulting revision hash. 
	 */
	public String getRevision() {
		return this.revision;
	}
	
	/**
	 * Create a new {@link de.oth.jit.repository.JitRevision} with the same
	 * commit message and revision. 
	 */
	public JitRevision clone() {
		return new JitRevision(this.message, this.revision);
	}
}
