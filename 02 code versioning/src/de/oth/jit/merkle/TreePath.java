package de.oth.jit.merkle;

import java.util.Stack;
import java.util.List;
import java.io.File;
import java.util.ArrayList;

final class TreePath {
	private final String path;
	private final Stack<String> stack;
	private final List<String> poppedParts;
	
	public TreePath(String path) {
		this.path = path.replace("\\\\", "/");
		
		this.poppedParts = new ArrayList<>();
		
		this.stack = new Stack<>();
		String[] pathParts = this.path.split("\\/");
		
		// insert all path parts into the stack in order to pop 
		// in the correct sort order later
		for (int i = pathParts.length - 1; i >= 0; i--) {
			this.stack.push(pathParts[i]);
		}
	}
	
	public String getFullPath() {
		return this.path;
	}
	
	public String getCurrentPath() {
		return String.join(File.separator, this.poppedParts);
	}
	
	public String pop() {
		String part = this.stack.pop();
		poppedParts.add(part);
		
		return part;
	}
	
	public int size() {
		return this.stack.size();
	}
}
