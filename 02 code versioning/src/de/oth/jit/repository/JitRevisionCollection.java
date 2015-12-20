package de.oth.jit.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class JitRevisionCollection implements Iterable<JitRevision>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final List<JitRevision> revisions;
	
	public JitRevisionCollection() {
		this(new ArrayList<>());
	}
	
	public JitRevisionCollection(List<JitRevision> revisions) {
		this.revisions = revisions;
	}
	
	public boolean contains(String revision) {
		for (JitRevision element : this.revisions) {
			if (element.getRevision().equals(revision)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void add(JitRevision revision) {
		this.revisions.add(revision);
	}
	
	public int size() {
		return this.revisions.size();
	}
	
	public JitRevisionCollection clone() {
		List<JitRevision> clonedRevisions = this.revisions.stream()
														  .map(e -> e.clone())
														  .collect(Collectors.toList());
		
		return new JitRevisionCollection(clonedRevisions);
	}

	@Override
	public Iterator<JitRevision> iterator() {
		return revisions.iterator();
	}
}
