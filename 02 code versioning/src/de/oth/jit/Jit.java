package de.oth.jit;

import de.oth.jit.repository.JitRevision;
import de.oth.jit.repository.JitRevisionCollection;
import de.oth.jit.repository.Repository;
import de.oth.jit.repository.RepositoryFactory;

public final class Jit {
	public static void main(String[] args) {
		// args = new String[] { "init" };
		// args = new String[] { "remove", "a/b/d" };
		// args = new String[] { "checkout", "9d7a84510d530598cf6e4963388c3122258f8f75" };
		args = new String[] { "tree" };
		// args = new String[] { "commit", "remove a" };
		
		String option = args.length > 0 ? args[0] : null;
		
		switch (args.length) {
			case 0:
				printHelp();
				break;
			
			case 1: 
				switch (option) {
					case "init":
						init();
						break;
					
					case "help":
						printHelp();
						break;
						
					case "staging":
						printStagingTree();
						break;
						
					case "history": 
						printRevisions();
						break;
						
					default: 
						warnInvalidArgument(option);
						break;
				}
				
				break;
			
			case 2:
				String argument = args[1];
				
				if (Repository.isInitialized()) {
					switch (option) {
						case "add": 
							add(argument);
							break;
							
						case "remove": 
							remove(argument);
							break;
							
						case "commit": 
							commit(argument);
							break;
							
						case "checkout": 
							checkout(argument);
							break;
							
						default: 
							warnInvalidArgument(option);
							break;
					}
				} else {
					System.out.println("Repository not initialized yet. ");
				}
				
				break;
				
			default: 
				warnInvalidArgumentCount();
				break;
		}
	}
	
	private static void warnInvalidArgument(String option) {
		System.out.println("Invalid argument. See \"java de.oth.jit.Jit help\" for further information. ");
	}
	
	private static void warnInvalidArgumentCount() {
		System.out.println("Invalid argument count passed. See \"java.de.oth.jit.Jit help\" for further information. ");
	}
	
	private static void printHelp() {
		System.out.println(
			"Jit code versioning\n" +
			"\n" + 
			"    help              Display this help\n" +
			"    staging           Display the staging tree\n" +
			"    history           Print all commits so far\n" +
			"\n" + 
			"    init              Initialize an empty repository\n" +
			"    add <path>        Add a path (recursively) to the repository\n" + 
			"    remove <path>     Remove a path from the repository\n" +
			"\n" + 
			"    commit <message>  Commit the current staging files\n" + 
			"    checkout <rev>    Checkout a previous version of this repository"
		);
	}
	
	public static void init() {
		if (Repository.isInitialized()) {
			System.out.println("Repository already initialized");
		} else {
			System.out.print("Initializing empty jit repository ...");
			Repository repo = new Repository();
			repo.init();
			System.out.println(" done");
		}
	}
	
	public static void add(String path) {
		try {
			Repository repo = RepositoryFactory.deserialize();

			repo.add(path);
			
			RepositoryFactory.serialize(repo);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	public static void remove(String path) {
		try {
			Repository repo = RepositoryFactory.deserialize();
			
			repo.remove(path);
			
			RepositoryFactory.serialize(repo);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	public static void commit(String message) {
		
		try {
			Repository repo = RepositoryFactory.deserialize();
			
			String revision = repo.commit(message);
			
			if (revision == null) {
				System.out.println("Nothing to commit");
			} else {
				System.out.println("HEAD is now at " + revision);

				RepositoryFactory.serialize(repo);
			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	public static void checkout(String revision) {
		try {
			Repository repo = RepositoryFactory.deserialize();
			
			repo.checkout(revision);
			
			RepositoryFactory.serialize(repo);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	public static void printStagingTree() {
		try {
			Repository repo = RepositoryFactory.deserialize();
			
			System.out.println(repo.getStagingTree());
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	public static void printRevisions() {
		if (Repository.isInitialized()) {
			try {
				Repository repo = RepositoryFactory.deserialize();
				
				JitRevisionCollection revisions = repo.getRevisions();
				
				if (revisions.size() > 0) {
					for (JitRevision revision : revisions) {
						System.out.println(revision.getRevision() + " " + revision.getMessage());
					}
				} else {
					System.out.println("No revisions yet");
				}
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
			}
		}
	}
}
