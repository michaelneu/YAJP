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

/**
 * This class represents a directory in the merkle tree, it's hash will be the
 * hash of its children's hashes. 
 * 
 * @author Michael Neu
 */
final class MerkleDirectory extends MerkleNode {
	private static final long serialVersionUID = 4631714828428011091L;
	
	private List<MerkleNode> children;
	
	/**
	 * Initialize the directory with a path and the path's basename. 
	 * 
	 * @param fullPath Which path to use
	 * @param name     Which basename to use
	 */
	public MerkleDirectory(String fullPath, String name) {
		super(fullPath, name);
		
		this.children = new ArrayList<>();
	}
	
	/**
	 * Find an element by its basename in the directory's children. 
	 * 
	 * @param element Which element to look for
	 * 
	 * @return The found element or null if there's no such element. 
	 */
	private MerkleNode find(String element) {
		return this.children.stream()
							.filter(e -> e.getName().equals(element))
							.findFirst()
							.orElse(null);
	}
	
	/**
	 * Add a file to the directory by following its path down the tree. 
	 * Required {@link de.oth.jit.merkle.MerkleDirectory}s will be created
	 * automatically. 
	 * 
	 * @param path The file to add
	 * 
	 * @throws ElementAddException
	 * @throws ElementUpdateException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
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
	
	/**
	 * Remove a file from the directory by following its path down the tree. 
	 * 
	 * @param path Which file to add
	 * 
	 * @throws ElementRemoveException
	 */
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
	
	/**
	 * Recursively remove empty directories in this node. 
	 */
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
	
	/**
	 * Create an ASCII styled tree of the merkle tree. 
	 * 
	 * @param padding Which white space padding to start with.
	 *  
	 * @return The ASCII tree
	 */
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
