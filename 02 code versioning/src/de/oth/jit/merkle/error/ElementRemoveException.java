package de.oth.jit.merkle.error;

/**
 * This exception is thrown when there's an error removing an element from the
 * merkle tree, e.g. when the tree doesn't contain the element. 
 * 
 * @author Michael Neu
 */
public final class ElementRemoveException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize the exception. 
	 * 
	 * @param file Which file caused the error
	 */
	public ElementRemoveException(String file) {
		super(String.format("Unable to remove '%s'", file));
	}
}
