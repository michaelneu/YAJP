package de.oth.jit.error;

public final class CommitFileCorruptedException extends Exception {
	private static final long serialVersionUID = 1L;

	public CommitFileCorruptedException(String message) {
		super(message);
	}
}
