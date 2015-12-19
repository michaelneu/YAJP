package de.oth.jit;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import de.oth.jit.commit.CommitElement;
import de.oth.jit.merkle.MerkleTree;

final class Repository implements Serializable {
	public static final String STAGING_FILE_PATH = ".jit/staging/staging.ser";
	private static final long serialVersionUID = 1L;
	
	private File baseDirectory, jit, objects, staging;
	private MerkleTree stagingTree;

	public Repository() {
		this.baseDirectory = new File(RepositoryUtils.getCurrentWorkingDirectory());
		
		this.jit = new File(this.baseDirectory, ".jit");
		this.objects = new File(this.jit, "objects");
		this.staging = new File(this.jit, "staging");
		
		this.stagingTree = new MerkleTree();
	}
	
	public static boolean isInitialized() {
		return new File(".jit").exists();
	}
	
	public void init() {
		if (!this.objects.exists()) {
			this.objects.mkdirs();
		}
		
		if (!this.staging.exists()) {
			this.staging.mkdir();
		}
		
		this.stagingTree = new MerkleTree();
	}
	
	public void add(String path) {
		this.stagingTree.add(path);
	}
	
	public void remove(String path) {
		this.stagingTree.remove(path);
	}
	
	public void commit(String message) {
		List<CommitElement> elements = stagingTree.flatten();
		
		for (CommitElement element : elements) {
			
		}
	}
	
	public void checkout(String revision) {
		
	}
	
	@Override
	public String toString() {
		return stagingTree.toString();
	}
}
