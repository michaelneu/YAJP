package de.oth.jit.merkle;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.oth.jit.commit.CommitDirectory;
import de.oth.jit.commit.Commitable;
import de.oth.jit.hashing.HashUtils;
import de.oth.jit.merkle.error.ElementAddException;
import de.oth.jit.merkle.error.ElementRemoveException;
import de.oth.jit.merkle.error.ElementUpdateException;

final class MerkleDirectory extends MerkleNode {
	private static final long serialVersionUID = 4631714828428011091L;
	
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
	
	public void add(TreePath path) throws ElementAddException, ElementUpdateException, NoSuchAlgorithmException, IOException {
		String top = path.pop();
		
		if (path.size() == 0) {
			// we reached the bottom of the path, the file
			MerkleNode node = find(top);
			
			if (node == null) {
				// insert the file into the tree
				
				this.children.add(new MerkleFile(path.getCurrentPath(), top));
			} else {
				// we already had a similar element, update if possible
				if (node instanceof MerkleFile) {
					((MerkleFile)node).updateContent();
				} else {
					throw new ElementUpdateException(path.getCurrentPath());
				}
			}
		} else {
			// we will need to follow the path to insert the file into the tree
			MerkleNode subtree = find(top);
			
			if (subtree == null) {
				// sutree not found in tree, add a new subtree
				
				MerkleDirectory directory = new MerkleDirectory(path.getCurrentPath(), top);
				directory.add(path);
				
				this.children.add(directory);
			} else if (subtree instanceof MerkleDirectory) {
				((MerkleDirectory)subtree).add(path);
			} else {
				throw new ElementAddException(path.getCurrentPath());
			}
		}
	}
	
	public void remove(TreePath path) throws ElementRemoveException {
		String top = path.pop();
		
		if (path.size() == 0) {
			// file must be in this directory, delete if possible
			MerkleNode file = find(top);
			
			if (file != null) {
				this.children.remove(file);
			} else {
				throw new ElementRemoveException(path.getFullPath());
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
	public String getHash() throws NoSuchAlgorithmException {
		String hashesCombined = "";
		
		for (MerkleNode node : this.children) {
			hashesCombined += node.getHash();
		}
		
		return HashUtils.hashString(hashesCombined);
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public List<Commitable> flatten() throws NoSuchAlgorithmException {
		List<Commitable> recursiveFlattenedChildren = new ArrayList<>();
		
		List<Commitable> layerChildren = new ArrayList<>();
		
		for (MerkleNode element : this.children) {
			List<Commitable> elementFlattened = element.flatten();
			
			if (elementFlattened.size() > 0) {
				layerChildren.add(elementFlattened.get(0));
			}
		}
		
		recursiveFlattenedChildren.add(new CommitDirectory(this.fullPath, getName(), getHash(), layerChildren));
		
		for (MerkleNode node : this.children) {
			recursiveFlattenedChildren.addAll(node.flatten());
		}
		
		return recursiveFlattenedChildren;
	}
	
	public String toString(String padding) {
		String output = padding.substring(0, padding.length() - 1) + "+-- " + getName() + "\n";
		
		padding += "   ";
		
		int count = 0;
		for (MerkleNode node : this.children) {
			count++;
			
			output += padding + "|\n";
			
			if (node instanceof MerkleDirectory) {
				MerkleDirectory directory = (MerkleDirectory)node;
				
				if (count == this.children.size()) {
					output += directory.toString(padding + " ");
				} else {
					output += directory.toString(padding + "|");
				}
			} else {
				output += padding + "+-- " + node.getName() + "\n";
			}
		}
		
		return output;
	}
}
