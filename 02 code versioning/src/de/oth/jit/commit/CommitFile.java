package de.oth.jit.commit;

public final class CommitFile extends CommitElement {
	private final String content;
	
	public CommitFile(String path, String name, String hash, String content) {
		super(path, name, hash);
		
		this.content = content;
	}
	
	@Override
	public String getCommitContent() {
		return this.content;
	}
}
