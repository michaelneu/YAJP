package de.oth.jit.commit;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * This object represents a directory in the file tree. 
 * 
 * @author Michael Neu
 */
public final class CommitDirectory extends CommitElement {
	private final List<Commitable> children;
	
	/**
	 * Initialize the directory. 
	 * 
	 * @param path     The real path of the directory
	 * @param name     The directory's basename
	 * @param hash     The directory's hash
	 * @param children The directory's children
	 */
	public CommitDirectory(String path, String name, String hash, List<Commitable> children) {
		super(path, name, hash);
		
		this.children = children;
	}
	
	/**
	 * Generate the content of the committed directory file. This will return 
	 * a list of elements inside the directory similar to this: 
	 * 
	 * <pre>
	 * Directory thisisthedirectoryshash directory
	 * File thisisthefileshash12345 filename
	 * </pre>
	 * 
	 * @return Prepared values for the committed file
	 * 
	 * @throws NoSuchAlgorithmException
	 */
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
