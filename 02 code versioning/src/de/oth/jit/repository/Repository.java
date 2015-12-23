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

/**
 * This class represents the JIT repository. It contains the staging tree and 
 * the history of commits done so far. 
 * 
 * @author Michael Neu
 */
public final class Repository implements Serializable {
	private static final long serialVersionUID = -7993375828050549804L;

	public static final String STAGING_FILE_PATH = ".jit/staging/staging.ser";
	
	private File baseDirectory, jit, objects, staging;
	private MerkleTree stagingTree;
	private boolean committed;
	private JitRevisionCollection revisions;

	/**
	 * Initialize the repository
	 */
	public Repository() {
		this.baseDirectory = new File(RepositoryUtils.getCurrentWorkingDirectory());
		
		this.jit = new File(this.baseDirectory, ".jit");
		this.objects = new File(this.jit, "objects");
		this.staging = new File(this.jit, "staging");
		
		this.stagingTree = new MerkleTree();
		
		this.committed = true;
		this.revisions = new JitRevisionCollection();
	}
	
	/**
	 * Check if the repository has already been initialized. 
	 * 
	 * @return Whether the repository has been initialized or not
	 */
	public static boolean isInitialized() {
		return new File(RepositoryUtils.getCurrentWorkingDirectory(), ".jit").exists();
	}
	
	/**
	 * Initialize the repository thus creating the <i>.jit</i>, <i>.jit/objects</i>
	 * and the <i>.jit/staging</i> directories. 
	 */
	public void init() {
		if (!this.objects.exists()) {
			this.objects.mkdirs();
		}
		
		if (!this.staging.exists()) {
			this.staging.mkdir();
		}
		
		this.stagingTree = new MerkleTree();
	}
	
	/**
	 * Add a file to the repository's staging area. 
	 * 
	 * @param path Which file to add
	 * 
	 * @throws ElementAddException
	 * @throws ElementUpdateException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public void add(String path) throws ElementAddException, ElementUpdateException, NoSuchAlgorithmException, IOException {
		this.committed = false;
		
		File file = new File(path);
		
		if (file.exists() && file.isFile()) {
			this.stagingTree.add(path);
		}
	}
	
	/**
	 * Remove a file from the repository's staging area. 
	 * 
	 * @param path Which file to remove
	 * 
	 * @throws ElementRemoveException
	 */
	public void remove(String path) throws ElementRemoveException {
		this.committed = false;
		
		File file = new File(path);
		
		if (file.exists() && file.isFile()) {
			this.stagingTree.remove(path);
		}
	}
	
	/**
	 * Commit the current staging area. 
	 * 
	 * @param message The message describing the commit
	 * 
	 * @return The commits revision hash
	 * 
	 * @throws StagingTreeCorruptedException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
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
	
	/**
	 * Restore a previous commit's workspace state. 
	 * 
	 * @param revision Which revision to restore
	 * 
	 * @throws RevisionNotFoundException
	 * @throws CommitFileCorruptedException
	 * @throws IOException
	 */
	public void checkout(String revision) throws RevisionNotFoundException, CommitFileCorruptedException, IOException {
		this.committed = false;
		
		if (this.revisions.contains(revision)) {
			RepositoryUtils.clearDirectory(this.baseDirectory);
			
			CommitUtils.restoreFromCommit(new File(this.objects, revision), this.baseDirectory);
		} else {
			throw new RevisionNotFoundException(revision);
		}
	}
	
	/**
	 * Get the ASCII representation of the staging tree. 
	 * 
	 * @return The staging tree's ASCII tree
	 */
	public String getStagingTree() {
		return stagingTree.toString();
	}
	
	/**
	 * Get the repository's commit history. 
	 * 
	 * @return The repository's commit history
	 */
	public JitRevisionCollection getRevisions() {
		return this.revisions.clone();
	}
}
