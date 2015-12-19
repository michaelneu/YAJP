package de.oth.jit.commit;

public class CommitFile extends CommitElement {
	private final String path, content, hash;
	
	public CommitFile(String path, String contents, String hash) {
		this.path = path;
		this.content = contents;
		this.hash = hash;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getHash() {
		return this.hash;
	}

	@Override
	public void createElement() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return String.format("File (%s): %s", this.path, this.hash);
	}
}
