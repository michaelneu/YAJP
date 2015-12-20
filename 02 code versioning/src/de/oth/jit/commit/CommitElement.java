package de.oth.jit.commit;

/**
 * Represents a generic element of the file tree. 
 * 
 * @author Michael Neu
 */
public abstract class CommitElement implements Commitable {
	protected final String path, hash, name;
	
	/**
	 * Initialize the generic attributes of the element. 
	 * 
	 * @param path The element's real path
	 * @param name The element's basename
	 * @param hash The element's hash
	 */
	public CommitElement(String path, String name, String hash) {
		this.path = path;
		this.name = name;
		this.hash = hash;
	}
	
	/**
	 * Get the element's real path. 
	 * 
	 * @return The element's real path. 
	 */
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
}
