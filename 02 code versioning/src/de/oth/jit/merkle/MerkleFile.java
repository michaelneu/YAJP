package de.oth.jit.merkle;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.oth.jit.commit.Commitable;
import de.oth.jit.commit.CommitFile;
import de.oth.jit.hashing.HashUtils;
import de.oth.jit.repository.RepositoryUtils;

/**
 * This class represents a file in the merkle tree. Its hash will be computed
 * from the file's actual content. 
 * 
 * @author Michael Neu
 */
final class MerkleFile extends MerkleNode {
	private static final long serialVersionUID = 1317971532386324058L;
	
	private String content;
	
	/**
	 * Initialize the file with it's absolute path and basename. 
	 * 
	 * @param fullPath The file's absolute path
	 * @param name     The file's basename
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public MerkleFile(String fullPath, String name) throws NoSuchAlgorithmException, IOException {
		super(fullPath, name);
		
		updateContent();
	}
	
	/**
	 * Re-read the file's content and recalculate the node's hash value. 
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public void updateContent() throws NoSuchAlgorithmException, IOException {
		byte[] fileContent = RepositoryUtils.readFileBytes(this.fullPath);
		
		this.content = new String(fileContent);
		this.hash = HashUtils.hashBytes(fileContent);
	}

	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public List<Commitable> flatten() {
		List<Commitable> flattened = new ArrayList<>();
		
		flattened.add(new CommitFile(this.fullPath, getName(), this.hash, this.content));
		
		return flattened;
	}
}
