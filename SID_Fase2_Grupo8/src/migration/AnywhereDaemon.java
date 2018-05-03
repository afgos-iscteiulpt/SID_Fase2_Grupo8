package migration;

public class AnywhereDaemon {
	
	private static final String DATABASE_NAME = "test_database";
	private static final String USERNAME = "dba";
	private static final String PASSWORD = "sql";
	
	private static ConnectionHandler ch = new ConnectionHandler();
	private static DatabaseManager db = new DatabaseManager(DATABASE_NAME, USERNAME, PASSWORD);
	
	public static void main(String[] args) {
		db.insertStatement("TestTable", "Name, DateThing", "'test', '1985-1-6 21:55:10'", ch);
		System.out.println("Should be done!");
	}
}
