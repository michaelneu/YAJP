package de.oth.jit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

final class RepositoryFactory {
	public static Repository deserialize() {
		File repoFile = new File(Repository.STAGING_FILE_PATH);
		
		if (repoFile.exists()) {
			try (FileInputStream file = new FileInputStream(repoFile)) {
				try (ObjectInputStream stream = new ObjectInputStream(file)) {
					return (Repository)stream.readObject();
				}
			} catch (Exception exception) {
				System.out.println("Error deserializing repository");
				System.exit(JitExitCode.DESERIALIZE_ERROR.getCode());
				
				return null;
			}
		} else {
			return new Repository();
		}
	}
	
	public static void serialize(Repository repo) {
		try (FileOutputStream file = new FileOutputStream(new File(Repository.STAGING_FILE_PATH))) {
			try (ObjectOutputStream stream = new ObjectOutputStream(file)) {
				stream.writeObject(repo);
			}
		} catch (Exception exception) {
			System.out.println("Error serializing repository");
			System.exit(JitExitCode.SERIALIZE_ERROR.getCode());
		}
	}
}
