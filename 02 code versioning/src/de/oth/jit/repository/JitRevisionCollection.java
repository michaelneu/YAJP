package de.oth.jit.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents a collection of {@link de.oth.jit.repository.JitRevision}
 * commits. It will be used to store the repository's commit history. 
 * 
 * @author Michael Neu
 */
public class JitRevisionCollection implements Iterable<JitRevision>, Serializable {
	private static final long serialVersionUID = 5941089379376891518L;
	
	private final List<JitRevision> revisions;
	
	/**
	 * Initialize the collection with an empty history. 
	 */
	public JitRevisionCollection() {
		this(new ArrayList<>());
	}
	
	/**
	 * Initialize the collection with a given history. 
	 * 
	 * @param revisions The history to use for the collection
	 */
	private JitRevisionCollection(List<JitRevision> revisions) {
		this.revisions = revisions;
	}
	
	/**
	 * Check if the collection contains a revision. 
	 * 
	 * @param revision Which revision to search
	 * 
	 * @return Whether the revision does exist or not. 
	 */
	public boolean contains(String revision) {
		for (JitRevision element : this.revisions) {
			if (element.getRevision().equals(revision)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Add a commit to the collection. 
	 * 
	 * @param revision Which commit to add
	 */
	public void add(JitRevision revision) {
		this.revisions.add(revision);
	}
	
	/**
	 * Get the size of this collection. 
	 * 
	 * @return The collection's size
	 */
	public int size() {
		return this.revisions.size();
	}

	/**
	 * Create a deep cloned {@link de.oth.jit.repository.JitRevisionCollection}. 
	 * 
	 * @return The cloned collection
	 */
	public JitRevisionCollection clone() {
		List<JitRevision> clonedRevisions = this.revisions.stream()
														  .map(e -> e.clone())
														  .collect(Collectors.toList());
		
		return new JitRevisionCollection(clonedRevisions);
	}

	@Override
	public Iterator<JitRevision> iterator() {
		return this.revisions.iterator();
	}
}
