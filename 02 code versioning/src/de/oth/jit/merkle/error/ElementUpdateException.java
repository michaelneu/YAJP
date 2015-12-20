package de.oth.jit.merkle.error;

/**
 * This exception is thrown when there is an error updating an existing elment
 * in the merkle tree. 
 * 
 * @author Michael Neu
 */
public final class ElementUpdateException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize the exception. 
	 * 
	 * @param file Which file caused the error
	 */
	public ElementUpdateException(String file) {
		super(String.format("Can't update file '%s'", file));
	}
}
