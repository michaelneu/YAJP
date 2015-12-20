package de.oth.jit.repository;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import de.oth.jit.commit.CommitUtils;
import de.oth.jit.commit.Commitable;
import de.oth.jit.error.CommitFileCorruptedException;
import de.oth.jit.error.RevisionNotFoundException;
import de.oth.jit.error.StagingTreeCorruptedException;
import de.oth.jit.merkle.MerkleTree;
import de.oth.jit.merkle.error.ElementAddException;
import de.oth.jit.merkle.error.ElementRemoveException;
import de.oth.jit.merkle.error.ElementUpdateException;

public final class Repository implements Serializable {
	public static final String STAGING_FILE_PATH = ".jit/staging/staging.ser";
	private static final long serialVersionUID = 1L;
	
	private File baseDirectory, jit, objects, staging;
	private MerkleTree stagingTree;
	private boolean committed;
	private JitRevisionCollection revisions;

	public Repository() {
		this.baseDirectory = new File(RepositoryUtils.getCurrentWorkingDirectory());
		
		this.jit = new File(this.baseDirectory, ".jit");
		this.objects = new File(this.jit, "objects");
		this.staging = new File(this.jit, "staging");
		
		this.stagingTree = new MerkleTree();
		
		this.committed = true;
		this.revisions = new JitRevisionCollection();
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
	
	public void add(String path) throws ElementAddException, ElementUpdateException, NoSuchAlgorithmException, IOException {
		this.committed = false;
		
		this.stagingTree.add(path);
	}
	
	public void remove(String path) throws ElementRemoveException {
		this.committed = false;
		
		this.stagingTree.remove(path);
	}
	
	public String commit(String message) throws StagingTreeCorruptedException, NoSuchAlgorithmException, IOException {
		if (!this.committed) {
			List<Commitable> elements = stagingTree.flatten();
			String revision = CommitUtils.createCommit(this.objects, elements, message);

			// add the commit to the revision list to allow displaying them later
			this.revisions.add(new JitRevision(message, revision));
			this.committed = true;
			
			return revision;
		} else {
			return null;
		}
	}
	
	public void checkout(String revision) throws RevisionNotFoundException, CommitFileCorruptedException, IOException {
		this.committed = false;
		
		if (this.revisions.contains(revision)) {
			// RepositoryUtils.clearDirectory(this.baseDirectory);
			CommitUtils.restoreFromCommit(new File(this.objects, revision), this.baseDirectory);
		} else {
			throw new RevisionNotFoundException(revision);
		}
	}
	
	public String getStagingTree() {
		return stagingTree.toString();
	}
	
	public JitRevisionCollection getRevisions() {
		return this.revisions.clone();
	}
}
