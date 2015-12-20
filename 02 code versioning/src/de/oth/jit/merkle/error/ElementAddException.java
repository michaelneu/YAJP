package de.oth.jit.merkle.error;

/**
 * This Exception is thrown when there's an error adding a file to the merkle
 * tree. 
 * 
 * @author Michael Neu
 */
public final class ElementAddException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize the exception. 
	 * 
	 * @param file Which file caused the error
	 */
	public ElementAddException(String file) {
		super(String.format("Unable to add '%s'", file));
	}
}
