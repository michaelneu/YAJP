package de.oth.jit.commit;

import java.security.NoSuchAlgorithmException;

import de.oth.jit.hashing.Hashable;

public interface Commitable extends Hashable {
	String getName();
	String getCommitContent() throws NoSuchAlgorithmException;
	String getIndicator();
}
