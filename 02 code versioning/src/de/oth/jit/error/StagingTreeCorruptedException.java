package de.oth.jit.error;

/**
 * This exception is thrown once the staging tree is corrupted thus making
 * insertion and deletion impossible. Possible scenarios: 
 * 
 * <ul>
 *     <li>A directory was added with the same name of a file in the tree</li>
 *     <li>The staging is completely empty</li>
 * </ul>
 * 
 * @author Michael Neu
 */
public final class StagingTreeCorruptedException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize the exception with a default message. 
	 */
	public StagingTreeCorruptedException() {
		this("Staging tree corrupted");
	}
	
	/**
	 * Initialize the exception. 
	 * 
	 * @param message A message describing what caused the exception
	 */
	public StagingTreeCorruptedException(String message) {
		super(message);
	}
}
