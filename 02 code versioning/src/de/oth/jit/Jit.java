package de.oth.jit;

public final class Jit {
	public static void main(String[] args) {
		// args = new String[] { "init" };
		// args = new String[] { "add", "a/b/c" };
		// args = new String[] { "add", "a/b/d" };
		args = new String[] { "commit", "my message" };
		
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
						
					case "tree":
						tree();
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
					System.exit(JitExitCode.REPO_NOT_INITIALIZED.getCode());
				}
				
				break;
				
			default: 
				warnInvalidArgumentCount();
				break;
		}
	}
	
	private static void warnInvalidArgument(String option) {
		System.out.println("Invalid argument. See \"java de.oth.jit.Jit help\" for further information. ");
		System.exit(JitExitCode.CLI_PARSER_ERROR.getCode());
	}
	
	private static void warnInvalidArgumentCount() {
		System.out.println("Invalid argument count passed. See \"java.de.oth.jit.Jit help\" for further information. ");
		System.exit(JitExitCode.CLI_PARSER_ERROR.getCode());
	}
	
	private static void printHelp() {
		System.out.println(
			"Jit code versioning\n" +
			"\n" + 
			"    help              Display this help\n" +
			"    tree              Display the staging tree\n" +
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
		Repository repo = RepositoryFactory.deserialize();
		
		repo.add(path);
		
		RepositoryFactory.serialize(repo);
	}
	
	public static void remove(String path) {
		Repository repo = RepositoryFactory.deserialize();
		
		repo.add(path);
		
		RepositoryFactory.serialize(repo);
	}
	
	public static void commit(String message) {
		
	}
	
	public static void checkout(String revision) {
		
	}
	
	public static void tree() {
		Repository repo = RepositoryFactory.deserialize();
		
		System.out.println(repo);
	}
}
