package de.oth.jit.commit;

import java.security.NoSuchAlgorithmException;

import de.oth.jit.error.StagingTreeCorruptedException;

public final class Commit implements Commitable {
	private final String message;
	private final CommitDirectory root;
	
	public Commit(String message, Commitable root) throws StagingTreeCorruptedException {
		this.message = message;
		
		if (root instanceof CommitDirectory) {
			this.root = (CommitDirectory)root;
		} else {
			this.root = null;
			
			throw new StagingTreeCorruptedException();
		}
	}
	
	@Override
	public String getHash() {
		return this.root.getHash();
	}

	@Override
	public String getName() {
		return this.message;
	}
	
	@Override
	public String getIndicator() {
		return "Commit";
	}

	@Override
	public String getCommitContent() throws NoSuchAlgorithmException {
		if (this.root != null) {
			return String.format("%s %s\n%s", getIndicator(), this.message, this.root.getChildrenCommitContent());
		} else {
			return String.format("%s %s", getIndicator(), this.message);
		}
	}
}
