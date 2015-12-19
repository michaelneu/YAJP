package de.oth.jit.commit;

public abstract class CommitElement implements Commitable {
	protected String path, hash, name;
	
	public CommitElement(String path, String name, String hash) {
		this.path = path;
		this.name = name;
		this.hash = hash;
	}
	
	public String getPath() {
		return this.path;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getHash() {
		return this.hash;
	}

	public static CommitElement restoreElement(String content) {
		return null;
	}
}
