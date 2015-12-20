package de.oth.jit.error;

public final class SerializerException extends Exception {
	private static final long serialVersionUID = 1L;

	public SerializerException(Throwable exception) {
		super(exception);
	}
}
