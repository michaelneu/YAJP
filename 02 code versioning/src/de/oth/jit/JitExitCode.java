package de.oth.jit;

public enum JitExitCode {
	// cli errors
	CLI_PARSER_ERROR(1),
	REPO_NOT_INITIALIZED(2),
	
	// jit-add errors
	ADD_FILE_ERROR(20),
	ADD_FILE_UPDATE_ERROR(21),
	
	// jit-remove errors
	REMOVE_FILE_ERROR(30),
	
	// hash erros
	HASH_ERROR(40),
	
	// io errors
	FILE_READ_ERROR(50),
	FILE_WRITE_ERROR(51),
	
	// serializer errors
	DESERIALIZE_ERROR(60),
	SERIALIZE_ERROR(61);
	
	private final int code;
	
	private JitExitCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
