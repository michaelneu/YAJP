package de.oth.jit.commit;

import java.security.NoSuchAlgorithmException;

import de.oth.jit.hashing.Hashable;

/**
 * An interface describing the fact that the object is capable of being 
 * committed to the objects directory of the repository. 
 * 
 * @author Michael Neu
 */
public interface Commitable extends Hashable {
	/**
	 * Get the name of the object. 
	 * 
	 * @return The object's name
	 */
	String getName();
	
	/**
	 * Get the committed file's content. Depending on the implementation this 
	 * will contain the file, the directory structure or general commit
	 * information. 
	 * 
	 * @return The committed file's content
	 * @throws NoSuchAlgorithmException
	 */
	String getCommitContent() throws NoSuchAlgorithmException;
	
	/**
	 * Get the indicator which type of file this element is. Possible values: 
	 * <ul>
	 *     <li>Directory</li>
	 *     <li>File</li>
	 *     <li>Commit</li>
	 * </ul>
	 * 
	 * @return One of the above indicators
	 */
	String getIndicator();
}
