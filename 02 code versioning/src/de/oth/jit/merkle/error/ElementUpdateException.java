package de.oth.jit.merkle.error;

public final class ElementUpdateException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ElementUpdateException(String file) {
		super(String.format("Can't update file '%s'", file));
	}
}
