package de.oth.jit.merkle;

import java.util.Stack;
import java.util.List;
import java.io.File;
import java.util.ArrayList;

/**
 * A wrappper to follow a path in the (merkle) tree. It allows to go down one 
 * node, view the current node's path and get the full path to go. 
 * 
 * @author Michael Neu
 */
final class TreePath {
	private final String path;
	private final Stack<String> stack;
	private final List<String> poppedParts;
	
	/**
	 * Initialize the path with a path string. The path may consist from 
	 * 
	 * <ul>
	 *     <li>unix-style paths: <pre>path/to/element</pre></li>
	 *     <li>nt-style paths: <pre>path\to\element</pre></li>
	 * </ul>
	 * 
	 * @param path Which path to follow down the tree
	 */
	public TreePath(String path) {
		this.path = path.replace("\\\\", "/");
		
		this.poppedParts = new ArrayList<>();
		
		this.stack = new Stack<>();
		String[] pathParts = this.path.split("\\/");
		
		// insert all path parts into the stack in order to pop 
		// in the correct order (reversed) later
		for (int i = pathParts.length - 1; i >= 0; i--) {
			this.stack.push(pathParts[i]);
		}
	}
	
	/**
	 * Get the full path to follow down the tree. 
	 * 
	 * @return The full path
	 */
	public String getFullPath() {
		return this.path;
	}
	
	/**
	 * Get the path of the current node. 
	 * 
	 * @return The current node's path
	 */
	public String getCurrentPath() {
		return String.join(File.separator, this.poppedParts);
	}
	
	/**
	 * Get the next element of the path to follow down the tree. 
	 * 
	 * @return The next path element
	 */
	public String pop() {
		String part = this.stack.pop();
		poppedParts.add(part);
		
		return part;
	}
	
	/**
	 * Get the count of elements in this path. 
	 * 
	 * @return The path size
	 */
	public int size() {
		return this.stack.size();
	}
}
