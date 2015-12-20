package de.oth.jit.error;

/**
 * This exception is thrown when it's tried to restore an unkown revision. 
 * 
 * @author Michael Neu
 */
public final class RevisionNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize the exception. 
	 * 
	 * @param revision Which revision caused the error
	 */
	public RevisionNotFoundException(String revision) {
		super(String.format("Revision '%s' not found", revision));
	}
}
