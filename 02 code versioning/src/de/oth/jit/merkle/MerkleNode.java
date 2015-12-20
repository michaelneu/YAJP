package de.oth.jit.merkle;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import de.oth.jit.commit.Commitable;
import de.oth.jit.hashing.Hashable;

abstract class MerkleNode implements Hashable, Serializable {
	private static final long serialVersionUID = 1L;
	
	protected final String fullPath, name;
	protected String hash;
	
	public MerkleNode(String fullPath, String name) {
		this.fullPath = fullPath;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getHash() throws NoSuchAlgorithmException {
		return this.hash;
	}
	
	public abstract int size();
	public abstract List<Commitable> flatten() throws NoSuchAlgorithmException;
}
