package de.oth.jit.merkle.error;

public final class ElementRemoveException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ElementRemoveException(String file) {
		super(String.format("Unable to remove '%s'", file));
	}
}
