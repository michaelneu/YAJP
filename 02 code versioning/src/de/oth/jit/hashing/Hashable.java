package de.oth.jit.hashing;

import java.security.NoSuchAlgorithmException;

/**
 * An interface describing the capability of an object being hashed. 
 * 
 * @author Michael Neu
 */
public interface Hashable {
	/**
	 * Get the hash representing the object. 
	 * 
	 * @return A hash string
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	String getHash() throws NoSuchAlgorithmException;
}
