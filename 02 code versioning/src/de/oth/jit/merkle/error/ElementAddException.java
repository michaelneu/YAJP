package de.oth.jit.merkle.error;

public final class ElementAddException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ElementAddException(String path) {
		super(String.format("Unable to add '%s'", path));
	}
}
