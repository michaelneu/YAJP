package de.oth.jit.commit;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public final class CommitDirectory extends CommitElement {
	private final List<Commitable> children;
	
	public CommitDirectory(String path, String name, String hash, List<Commitable> children) {
		super(path, name, hash);
		
		this.children = children;
	}
	
	protected String getChildrenCommitContent() throws NoSuchAlgorithmException {
		String content = "";

		for (Commitable child : this.children) {
			content += String.format("%s %s %s\n", child.getIndicator(), child.getHash(), child.getName());
		}
		
		return content;
	}
	
	@Override
	public String getIndicator() {
		return "Directory";
	}

	@Override
	public String getCommitContent() throws NoSuchAlgorithmException {
		return getIndicator() + "\n" + getChildrenCommitContent();
	}
}
