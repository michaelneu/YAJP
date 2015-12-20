package de.oth.jit.merkle;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import de.oth.jit.commit.Commitable;
import de.oth.jit.merkle.error.ElementAddException;
import de.oth.jit.merkle.error.ElementRemoveException;
import de.oth.jit.merkle.error.ElementUpdateException;

/**
 * This class provides a 
 * <a href="https://en.wikipedia.org/wiki/Merkle_tree">merkle tree</a> 
 * implementation. It will be used as the repository's staging tree. 
 * 
 * @author Michael Neu
 */
public final class MerkleTree implements Serializable {
	private static final long serialVersionUID = -6554472469165858750L;
	
	private MerkleDirectory root;
	
	/**
	 * Initialize the empty merkle tree. 
	 */
	public MerkleTree() {
		this.root = new MerkleDirectory("jit repository", ".");
	}
	
	/**
	 * Add a file to the merkle tree. 
	 * 
	 * @param path Which file to add
	 * 
	 * @throws ElementAddException
	 * @throws ElementUpdateException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public void add(String path) throws ElementAddException, ElementUpdateException, NoSuchAlgorithmException, IOException {
		this.root.add(new TreePath(path));
	}
	
	/**
	 * Remove a file from the merkle tree. 
	 * 
	 * @param path Which file to remove
	 * 
	 * @throws ElementRemoveException
	 */
	public void remove(String path) throws ElementRemoveException {
		this.root.remove(new TreePath(path));
		this.root.removeEmptyDirectories();
	}

	/**
	 * Flatten the tree to create the set of the elements it contains. The 
	 * resulting set of elments will be {@link de.oth.jit.commit.Commitable}. 
	 * 
	 * @return The flattened tree
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public List<Commitable> flatten() throws NoSuchAlgorithmException {
		return this.root.flatten();
	}
	
	@Override
	public String toString() {
		String tree = this.root.toString(" ");
		tree = tree.replaceFirst("\\+-- ", "    ")
				   .replaceAll("(?m)^\\s{4}", "");
		
		return tree;
	}
}
