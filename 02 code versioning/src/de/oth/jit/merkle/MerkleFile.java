package de.oth.jit.merkle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.oth.jit.JitExitCode;
import de.oth.jit.commit.Commitable;
import de.oth.jit.commit.CommitFile;
import de.oth.jit.hashing.HashUtils;

final class MerkleFile extends MerkleNode {
	private static final long serialVersionUID = 1L;
	
	private String content;
	
	public MerkleFile(String fullPath, String name) {
		super(fullPath, name);
		
		updateContent();
	}
	
	public void updateContent() {
		try {
			byte[] fileContent = Files.readAllBytes(Paths.get(this.fullPath));
			
			this.content = new String(fileContent);
			this.hash = HashUtils.hashBytes(fileContent);
		} catch (IOException exception) {
			exception.printStackTrace();
			System.out.println(String.format("Unable to read file '%s'", this.fullPath));
			System.exit(JitExitCode.FILE_READ_ERROR.getCode());
		}
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
