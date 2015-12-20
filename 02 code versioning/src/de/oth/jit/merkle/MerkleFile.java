package de.oth.jit.merkle;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.oth.jit.commit.Commitable;
import de.oth.jit.commit.CommitFile;
import de.oth.jit.hashing.HashUtils;
import de.oth.jit.repository.RepositoryUtils;

final class MerkleFile extends MerkleNode {
	private static final long serialVersionUID = 1L;
	
	private String content;
	
	public MerkleFile(String fullPath, String name) throws NoSuchAlgorithmException, IOException {
		super(fullPath, name);
		
		updateContent();
	}
	
	public void updateContent() throws NoSuchAlgorithmException, IOException {
		byte[] fileContent = RepositoryUtils.readFileBytes(this.fullPath);
		
		this.content = new String(fileContent);
		this.hash = HashUtils.hashBytes(fileContent);
	}
	
	@Override
	public String toString() {
		return String.format("{%s \\\\ %s}, align=center", this.fullPath, this.hash);
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
