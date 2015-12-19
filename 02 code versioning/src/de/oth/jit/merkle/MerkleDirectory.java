package de.oth.jit.merkle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.oth.jit.JitExitCode;
import de.oth.jit.commit.CommitDirectory;
import de.oth.jit.commit.CommitElement;
import de.oth.jit.hashing.HashUtils;

class MerkleDirectory extends MerkleNode {
	private static final long serialVersionUID = 1L;
	
	private List<MerkleNode> children;
	
	public MerkleDirectory(String fullPath, String name) {
		super(fullPath, name);
		
		this.children = new ArrayList<>();
	}
	
	private MerkleNode find(String element) {
		return this.children.stream()
							.filter(e -> e.getName().equals(element))
							.findFirst()
							.orElse(null);
	}
	
	public void add(TreePath path) {
		String top = path.pop();
		
		if (path.size() == 0) {
			// we reached the bottom of the path, the file
			MerkleNode node = find(top);
			
			if (node == null) {
				// insert the file into the tree
				
				this.children.add(new MerkleFile(path.getFullPath(), top));
			} else {
				// we already head a similar element, update if possible
				if (node instanceof MerkleFile) {
					((MerkleFile)node).updateContent();
				} else {
					System.out.println(String.format("Can't update file '%s'", path.getFullPath()));
					System.exit(JitExitCode.ADD_FILE_UPDATE_ERROR.getCode());
				}
			}
		} else {
			// we will need to follow the path to insert the file into the tree
			MerkleNode subtree = find(top);
			
			if (subtree == null) {
				// sutree not found in tree, add a new subtree
				MerkleDirectory directory = new MerkleDirectory(path.getFullPath(), top);
				directory.add(path);
				
				this.children.add(directory);
			} else if (subtree instanceof MerkleDirectory) {
				((MerkleDirectory)subtree).add(path);
			} else {
				System.out.println(String.format("Unable to add '%s'", path.getFullPath()));
				System.exit(JitExitCode.ADD_FILE_ERROR.getCode());
			}
		}
	}
	
	public void remove(TreePath path) {
		String top = path.pop();
		
		if (path.size() == 0) {
			// file must be in this directory, delete if possible
			MerkleNode file = find(top);
			
			if (file != null) {
				this.children.remove(file);
			} else {
				System.out.println(String.format("Unable to remove '%s'", file));
				System.exit(JitExitCode.REMOVE_FILE_ERROR.getCode());
			}
		} else {
			// follow the path down the tree
			MerkleNode directory = find(top);
			
			if (directory != null && directory instanceof MerkleDirectory) {
				((MerkleDirectory)directory).remove(path);
			}
		}
	}
	
	@Override
	public int size() {
		return this.children.size();
	}
	
	public void removeEmptyDirectories() {
		for (MerkleDirectory directory : this.children.stream()
			                                          .filter(e -> e instanceof MerkleDirectory)
			                                          .map(e -> (MerkleDirectory)e)
			                                          .collect(Collectors.toList())) {
			directory.removeEmptyDirectories();
		}
		
		this.children = this.children.stream()
		                             .filter(e -> e.size() > 0)
		                             .collect(Collectors.toList());
	}
	
	public boolean contains(String element) {
		long count = this.children.stream()
								  .filter(e -> e.getName().equals(element))
								  .count();
		
		return count == 1;
	}

	@Override
	public String getHash() {
		String hashesCombined = String.join("", this.children.stream()
															 .map(e -> e.getHash())
															 .collect(Collectors.toList()));
		
		return HashUtils.hashString(hashesCombined);
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public List<CommitElement> flatten() {
		List<CommitElement> children = new ArrayList<>();
		
		children.add(new CommitDirectory(this.fullPath, this.getHash()));
		
		for (MerkleNode node : this.children) {
			children.addAll(node.flatten());
		}
		
		return children;
	}
	
	@Override
	public String toString() {
		String output = "";
		
		for (MerkleNode node : this.children) {
			output += String.format("[%s] ", node.toString());
		}
		
		return String.format("{%s \\\\ %s}, draw, align=center %s", getName(), getHash(), output);
	}
}
