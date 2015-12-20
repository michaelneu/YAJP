package de.oth.jit.repository;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public final class RepositoryUtils {
	public static String getCurrentWorkingDirectory() {
		Path currentPath = Paths.get("");
		return currentPath.toAbsolutePath().toString();
	}
	
	public static byte[] readFileBytes(String file) throws IOException {
		return readFileBytes(new File(file));
	}
	
	public static byte[] readFileBytes(File file) throws IOException {
		return Files.readAllBytes(file.toPath());
	}
	
	public static List<String> readFileLines(File file) throws IOException {
		return Files.readAllLines(file.toPath());
	}
	
	public static void writeFile(File file, String content) throws IOException {
		Files.write(file.toPath(), content.getBytes());
	}
	
	public static void copyFile(File src, File dest) throws IOException {
		Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public static void clearDirectory(File directory) throws SecurityException {
		directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File element) {
				// only delete directories != ./.jit
				if (!(element.getParentFile().equals(directory) && element.getName().equals(".jit"))) {
					// recursively delete directories
					if (element.isDirectory()) {
						element.listFiles(this);
					}
					
					element.delete();
				}
				
				return false;
			}
		});
	}
}
