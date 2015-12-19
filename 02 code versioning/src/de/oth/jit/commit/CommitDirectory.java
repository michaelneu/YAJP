package de.oth.jit.commit;

public class CommitDirectory extends CommitElement {
	private final String path, hash;
	
	public CommitDirectory(String path, String hash) {
		this.path = path;
		this.hash = hash;
	}

	@Override
	public void createElement() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return String.format("Directory (%s): %s", this.path, this.hash);
	}
}
