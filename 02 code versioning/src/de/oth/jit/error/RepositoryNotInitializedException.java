package de.oth.jit.error;

public class RepositoryNotInitializedException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public RepositoryNotInitializedException(String message) {
		super(message);
	}
}
