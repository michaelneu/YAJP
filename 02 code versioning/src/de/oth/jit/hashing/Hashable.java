package de.oth.jit.hashing;

import java.security.NoSuchAlgorithmException;

public interface Hashable {
	String getHash() throws NoSuchAlgorithmException;
}
