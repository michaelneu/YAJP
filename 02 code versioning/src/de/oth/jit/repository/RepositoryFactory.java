package de.oth.jit.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.oth.jit.error.SerializerException;

public final class RepositoryFactory {
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
