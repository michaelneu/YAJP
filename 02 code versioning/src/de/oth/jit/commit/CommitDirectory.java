package de.oth.jit.commit;

import java.util.List;

public final class CommitDirectory extends CommitElement {
	private final List<Commitable> children;
	
	public CommitDirectory(String path, String name, String hash, List<Commitable> children) {
		super(path, name, hash);
		
		this.children = children;
	}
	
	protected String getChildrenCommitContent() {
		String content = "";

		for (Commitable child : this.children) {
			content += String.format("%s %s\n", child.getHash(), child.getName());
		}
		
		return content;
	}

	@Override
	public String getCommitContent() {
		return "Directory\n" + getChildrenCommitContent();
	}
}
