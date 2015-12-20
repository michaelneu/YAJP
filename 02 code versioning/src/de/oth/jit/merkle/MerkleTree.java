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
	private static final long serialVersionUID = 1L;
	
	private MerkleDirectory root;
	
	public MerkleTree() {
		root = new MerkleDirectory("jit repository", ".");
	}
	
	public void add(String path) throws ElementAddException, ElementUpdateException, NoSuchAlgorithmException, IOException {
		root.add(new TreePath(path));
	}
	
	public void remove(String path) throws ElementRemoveException {
		root.remove(new TreePath(path));
		root.removeEmptyDirectories();
	}

	public List<Commitable> flatten() throws NoSuchAlgorithmException {
		return root.flatten();
	}

	@Override
	public String toString() {
		return String.format("[%s]", root.toString());
	}
}
