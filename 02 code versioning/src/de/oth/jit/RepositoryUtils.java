package de.oth.jit;

import java.nio.file.Path;
import java.nio.file.Paths;

final class RepositoryUtils {
	public static String getCurrentWorkingDirectory() {
		Path currentPath = Paths.get("");
		return currentPath.toAbsolutePath().toString();
	}
}
