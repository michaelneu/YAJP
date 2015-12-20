package de.oth.jit.merkle;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import de.oth.jit.commit.Commitable;
import de.oth.jit.hashing.Hashable;

/**
 * Represents a generic node in the merkle tree. 
 * 
 * @author Michael Neu
 */
abstract class MerkleNode implements Hashable, Serializable {
	private static final long serialVersionUID = -2269390795642248508L;
	
	protected final String fullPath, name;
	protected String hash;
	
	/**
	 * Initialize the node with a path and the basename of the node. 
	 * 
	 * @param fullPath Which path to use
	 * @param name     Which basename to use
	 */
	public MerkleNode(String fullPath, String name) {
		this.fullPath = fullPath;
		this.name = name;
	}
	
	/**
	 * Get the node's basename. 
	 * 
	 * @return The elements basename
	 */
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getHash() throws NoSuchAlgorithmException {
		return this.hash;
	}
	
	/**
	 * Get the count of children in this node. 
	 * 
	 * @return The size of the node
	 */
	public abstract int size();

	/**
	 * Flatten the node to create the set of the elements it contains. The 
	 * resulting set of elements will be {@link de.oth.jit.commit.Commitable}. 
	 * 
	 * @return The flattened node
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public abstract List<Commitable> flatten() throws NoSuchAlgorithmException;
}
