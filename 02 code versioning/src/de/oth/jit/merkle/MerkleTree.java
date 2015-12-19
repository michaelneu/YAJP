package de.oth.jit.merkle;

import java.io.Serializable;
import java.util.List;

import de.oth.jit.commit.Commitable;

public final class MerkleTree implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private MerkleDirectory root;
	
	public MerkleTree() {
		root = new MerkleDirectory("jit repository", ".");
	}
	
	public void add(String path) {
		root.add(new TreePath(path));
	}
	
	public void remove(String path) {
		root.remove(new TreePath(path));
		root.removeEmptyDirectories();
	}

	public List<Commitable> flatten() {
		return root.flatten();
	}

	@Override
	public String toString() {
		return String.format("[%s]", root.toString());
	}
}
