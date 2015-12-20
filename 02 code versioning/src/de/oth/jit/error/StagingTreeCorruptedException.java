package de.oth.jit.error;

public final class StagingTreeCorruptedException extends Exception {
	private static final long serialVersionUID = 1L;

	public StagingTreeCorruptedException() {
		this("Staging tree corrupted");
	}
	
	public StagingTreeCorruptedException(String message) {
		super(message);
	}
}
