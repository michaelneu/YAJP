package de.oth.jit.commit;

/**
 * This object defines a file in the file tree. 
 * 
 * @author Michael Neu
 */
public final class CommitFile extends CommitElement {
	private final String content;
	
	/**
	 * Initialize the file
	 * 
	 * @param path    The file's real path
	 * @param name    The file's basename
	 * @param hash    The file's hash
	 * @param content The file's content
	 */
	public CommitFile(String path, String name, String hash, String content) {
		super(path, name, hash);
		
		this.content = content;
	}

	@Override
	public String getIndicator() {
		return "File";
	}
	
	@Override
	public String getCommitContent() {
		return this.content;
	}
}
