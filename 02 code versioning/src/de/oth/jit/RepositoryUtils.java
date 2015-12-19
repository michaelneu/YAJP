package de.oth.jit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class RepositoryUtils {
	public static String getCurrentWorkingDirectory() {
		Path currentPath = Paths.get("");
		return currentPath.toAbsolutePath().toString();
	}
	
	public static void writeFile(File file, String content) {
		try {
			Files.write(file.toPath(), content.getBytes());
		} catch (IOException exception) {
			System.out.println("Unable to write commit file");
			System.exit(JitExitCode.FILE_WRITE_ERROR.getCode());
		}
	}
}
