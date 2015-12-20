package de.oth.jit.error;

/**
 * This exception is thrown when there's an error processing a committed file
 * to restore a previous revision's file tree. 
 * 
 * @author Michael Neu
 */
public final class CommitFileCorruptedException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize the exception. 
	 * 
	 * @param message The message describing what caused the error
	 */
	public CommitFileCorruptedException(String message) {
		super(message);
	}
}
