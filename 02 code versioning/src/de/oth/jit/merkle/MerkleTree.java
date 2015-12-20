package de.oth.jit.merkle;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import de.oth.jit.commit.Commitable;
import de.oth.jit.merkle.error.ElementAddException;
import de.oth.jit.merkle.error.ElementRemoveException;
import de.oth.jit.merkle.error.ElementUpdateException;

public final class MerkleTree implements Serializable {
	private static final long serialVersionUID = -6554472469165858750L;
	
	private MerkleDirectory root;
	
	public MerkleTree() {
		this.root = new MerkleDirectory("jit repository", ".");
	}
	
	public void add(String path) throws ElementAddException, ElementUpdateException, NoSuchAlgorithmException, IOException {
		this.root.add(new TreePath(path));
	}
	
	public void remove(String path) throws ElementRemoveException {
		this.root.remove(new TreePath(path));
		this.root.removeEmptyDirectories();
	}

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
