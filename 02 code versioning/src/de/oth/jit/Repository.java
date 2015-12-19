package de.oth.jit;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.oth.jit.commit.Commit;
import de.oth.jit.commit.Commitable;
import de.oth.jit.merkle.MerkleTree;

final class Repository implements Serializable {
	public static final String STAGING_FILE_PATH = ".jit/staging/staging.ser";
	private static final long serialVersionUID = 1L;
	
	private File baseDirectory, jit, objects, staging;
	private MerkleTree stagingTree;
	private boolean commited;
	private List<JitRevision> revisions;

	public Repository() {
		this.baseDirectory = new File(RepositoryUtils.getCurrentWorkingDirectory());
		
		this.jit = new File(this.baseDirectory, ".jit");
		this.objects = new File(this.jit, "objects");
		this.staging = new File(this.jit, "staging");
		
		this.stagingTree = new MerkleTree();
		
		this.commited = true;
		this.revisions = new ArrayList<>();
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
		this.commited = false;
		
		this.stagingTree.add(path);
	}
	
	public void remove(String path) {
		this.commited = false;
		
		this.stagingTree.remove(path);
	}
	
	public String commit(String message) {
		if (!this.commited) {
			List<Commitable> elements = stagingTree.flatten();
			
			if (elements.size() > 0) {
				// the first element will be the root of the repository, it _will_ be there
				Commitable commitInformation = elements.remove(0);
				String revision = commitInformation.getHash();
				
				Commit commit = new Commit(message, commitInformation);
				elements.add(commit);
				
				// continue with the rest of the commited files
				for (Commitable element : elements) {
					File file = new File(this.objects, element.getHash());
					System.out.println("Writing " + element.getHash() + "(" + element.getName() + ")");
					
					if (!file.exists() || true) {
						RepositoryUtils.writeFile(file, element.getCommitContent());
					}
				}
				
				this.revisions.add(new JitRevision(message, revision));
				this.commited = true;
				
				return revision;
			} else {
				this.commited = true;
			}
		}
		
		return null;
	}
	
	public void checkout(String revision) {
		this.commited = false;
	}
	
	public String getStagingTree() {
		return stagingTree.toString();
	}
	
	public List<JitRevision> getRevisions() {
		return this.revisions;
	}
}
