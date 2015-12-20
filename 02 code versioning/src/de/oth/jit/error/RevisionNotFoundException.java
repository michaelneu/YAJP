package de.oth.jit.error;

public final class RevisionNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public RevisionNotFoundException(String revision) {
		super(String.format("Revision '%s' not found", revision));
	}
}
