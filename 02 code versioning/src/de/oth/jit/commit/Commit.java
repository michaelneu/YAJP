package de.oth.jit.commit;

import de.oth.jit.JitExitCode;

public final class Commit implements Commitable {
	private final String message;
	private final CommitDirectory root;
	
	public Commit(String message, Commitable root) {
		this.message = message;
		
		if (root instanceof CommitDirectory) {
			this.root = (CommitDirectory)root;
		} else {
			this.root = null;
			System.out.println("Staging tree corrupted. ");
			System.exit(JitExitCode.STAGING_TREE_CORRUPTED.getCode());
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
	public String getCommitContent() {
		return String.format("Commit %s\n%s", this.message, this.root.getChildrenCommitContent());
	}
}
