package de.oth.jit.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.oth.jit.error.SerializerException;

/**
 * A factory to save and restore repository information. 
 * 
 * @author Michael Neu
 */
public final class RepositoryFactory {
	/**
	 * Restore the repository from the staging file. 
	 * 
	 * @return The restored repository or a new repository if the file doesn't 
	 *         exist.
	 *  
	 * @throws SerializerException
	 */
	public static Repository deserialize() throws SerializerException {
		File repoFile = new File(Repository.STAGING_FILE_PATH);
		
		if (repoFile.exists()) {
			try (FileInputStream file = new FileInputStream(repoFile)) {
				try (ObjectInputStream stream = new ObjectInputStream(file)) {
					return (Repository)stream.readObject();
				}
			} catch (Exception exception) {
				throw new SerializerException(exception);
			}
		} else {
			return new Repository();
		}
	}
	
	/**
	 * Save a repository to the staging file. 
	 * 
	 * @param repo The repo to save
	 * 
	 * @throws SerializerException
	 */
	public static void serialize(Repository repo) throws SerializerException {
		try (FileOutputStream file = new FileOutputStream(new File(Repository.STAGING_FILE_PATH))) {
			try (ObjectOutputStream stream = new ObjectOutputStream(file)) {
				stream.writeObject(repo);
			}
		} catch (Exception exception) {
			throw new SerializerException(exception);
		}
	}
}
