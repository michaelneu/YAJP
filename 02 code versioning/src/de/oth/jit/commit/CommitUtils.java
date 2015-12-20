package de.oth.jit.commit;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import de.oth.jit.error.CommitFileCorruptedException;
import de.oth.jit.error.StagingTreeCorruptedException;
import de.oth.jit.repository.RepositoryUtils;

public class CommitUtils {
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
						
						restoreFromFile(directoryFileContents, directoryRealPath);
						break;
					
					case "File":
						File fileCommitPath = new File(currentPath, information[1]),
							fileRealPath = new File(currentPath, information[2]);
						
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
