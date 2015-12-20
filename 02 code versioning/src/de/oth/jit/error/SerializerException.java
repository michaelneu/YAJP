package de.oth.jit.error;

/**
 * A wrapper exception to signal there was something wrong with de-/serializing
 * the repository information. This exception will be thrown if there are 
 * different versions of {@link de.oth.jit.repository.Repository}, 
 * {@link de.oth.jit.merkle.MerkleTree}, {@link de.oth.jit.merkle.MerkleNode},
 * {@link de.oth.jit.merkle.MerkleFile}, 
 * {@link de.oth.jit.merkle.MerkleDirectory}, 
 * {@link de.oth.jit.repository.JitRevision} or 
 * {@link de.oth.jit.repository.JitRevisionCollection} between serializing and
 * deserializing. 
 * 
 * @author Michael Neu
 */
public final class SerializerException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize the exception with the thrown base exception. 
	 * 
	 * @param exception The exception that caused this exception
	 */
	public SerializerException(Throwable exception) {
		super(exception);
	}
}
