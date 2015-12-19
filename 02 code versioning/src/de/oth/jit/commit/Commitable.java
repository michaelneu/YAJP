package de.oth.jit.commit;

import de.oth.jit.hashing.Hashable;

public interface Commitable extends Hashable {
	public abstract String getName();
	public abstract String getCommitContent();
}
