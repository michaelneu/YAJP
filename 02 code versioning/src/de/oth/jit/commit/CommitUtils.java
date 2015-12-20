package de.oth.jit.commit;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import de.oth.jit.error.CommitFileCorruptedException;
import de.oth.jit.error.StagingTreeCorruptedException;
import de.oth.jit.repository.RepositoryUtils;

/**
 * This class provides utilities to manage commits. It supports creating and
 * restoring committed files. 
 * 
 * @author Michael Neu
 */
public class CommitUtils {
	/**
	 * Create a revision from a list of commitable files.  
	 * 
	 * @param objectsDirectory In which directory to save the committed files
	 * @param elements         Which files to commit
	 * @param message          Which message to use for the commit
	 * 
	 * @return The commit's revision 
	 * 
	 * @throws StagingTreeCorruptedException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String createCommit(File objectsDirectory, List<Commitable> elements, String message) throws StagingTreeCorruptedException, NoSuchAlgorithmException, IOException {
		if (elements.size() > 0) {
			// the first element will be the root of the repository, it _will_ be there
			Commitable commitInformation = elements.remove(0);
			String revision = commitInformation.getHash();
			
			Commit commit = new Commit(message, commitInformation);
			elements.add(commit);
			
			// continue with the rest of the commited files
			for (Commitable element : elements) {
				File file = new File(objectsDirectory, element.getHash());
				
				if (!file.exists()) {
					RepositoryUtils.writeFile(file, element.getCommitContent());
				}
			}
			
			return revision;
		} else {
			// if the staging tree would ever be completely empty we'd have a bigger problem
			// than unset properties. empty tree = merkle-tree is corrupted.
			
			throw new StagingTreeCorruptedException("The staging tree is completely empty, it might be corrupted");
		}
	}
	
	/**
	 * Restore the state of a previous revision by restoring from the 
	 * revision's commit file. 
	 * 
	 * @param commitFile    The file containing the commit information
	 * @param baseDirectory The repository's base directory
	 * 
	 * @throws CommitFileCorruptedException
	 * @throws IOException
	 */
	public static void restoreFromCommit(File commitFile, File baseDirectory) throws CommitFileCorruptedException, IOException {
		if (commitFile.exists()) {
			List<String> commitLines = RepositoryUtils.readFileLines(commitFile);
			
			if (commitLines.size() > 1 && commitLines.get(0).startsWith("Commit")) {
				restoreFromFile(commitLines, baseDirectory);
			} else {
				throw new CommitFileCorruptedException("Invalid format");
			}
		}
	}
	
	/**
	 * Restore the state of a previous revision from a file containing 
	 * information about the structure of a directory. 
	 * 
	 * @param content     The file's content
	 * @param currentPath At which part we're right now
	 * 
	 * @throws CommitFileCorruptedException
	 * @throws IOException
	 */
	private static void restoreFromFile(List<String> content, File currentPath) throws CommitFileCorruptedException, IOException {
		// remove the first line as it's not relevant for restoring
		content.remove(0);
		
		for (String line : content) {
			String[] information = line.split(" ");
			
			if (information.length == 3) {
				switch (information[0]) {
					case "Directory":
						File directoryCommitPath = new File(currentPath, information[1]),
							directoryRealPath = new File(currentPath, information[2]);
						
						List<String> directoryFileContents = RepositoryUtils.readFileLines(directoryCommitPath);
						
						// we don't need to create directories because a file inside the directory will do this
						restoreFromFile(directoryFileContents, directoryRealPath);
						break;
					
					case "File":
						File fileCommitPath = new File(currentPath, information[1]),
							fileRealPath = new File(currentPath, information[2]);
						
						fileRealPath.mkdirs();
						RepositoryUtils.copyFile(fileCommitPath, fileRealPath);
						break;
						
					default: 
						throw new CommitFileCorruptedException(String.format("Invalid element type. Expected 'File' or 'Directory', got '%s'", information[0]));
				}
			} else {
				throw new CommitFileCorruptedException("Malformed commit file");
			}
		}
	}
}
