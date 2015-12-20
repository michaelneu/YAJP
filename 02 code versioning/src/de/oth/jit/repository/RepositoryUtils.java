package de.oth.jit.repository;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * This class provides wrapper utilities to manage repository I/O. 
 * 
 * @author Michael Neu
 */
public final class RepositoryUtils {
	/**
	 * Get the absolute path to the current working directory. 
	 * 
	 * @return The current working directory
	 */
	public static String getCurrentWorkingDirectory() {
		Path currentPath = Paths.get("");
		return currentPath.toAbsolutePath().toString();
	}
	
	/**
	 * Read all bytes from a file. 
	 * 
	 * @param path The path to the file
	 * 
	 * @return The file's bytes
	 * 
	 * @throws IOException
	 */
	public static byte[] readFileBytes(String path) throws IOException {
		return readFileBytes(new File(path));
	}

	/**
	 * Read all bytes from a file. 
	 * 
	 * @param file The file to read from
	 * 
	 * @return The file's bytes
	 * 
	 * @throws IOException
	 */
	public static byte[] readFileBytes(File file) throws IOException {
		return Files.readAllBytes(file.toPath());
	}

	/**
	 * Read all lines from a file. 
	 * 
	 * @param file The file to read from
	 * 
	 * @return The file's lines
	 * 
	 * @throws IOException
	 */
	public static List<String> readFileLines(File file) throws IOException {
		return Files.readAllLines(file.toPath());
	}
	
	/**
	 * Write content to a file. 
	 * 
	 * @param file    Which file to write to
	 * @param content What to write
	 * 
	 * @throws IOException
	 */
	public static void writeFile(File file, String content) throws IOException {
		Files.write(file.toPath(), content.getBytes());
	}
	
	/**
	 * Copy a file and replace the existing file. 
	 * 
	 * @param src
	 * @param dest
	 * 
	 * @throws IOException
	 */
	public static void copyFile(File src, File dest) throws IOException {
		Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * Recursively delete the contents of a directory. 
	 * 
	 * @param directory Which directory to clear
	 * 
	 * @throws SecurityException
	 */
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
