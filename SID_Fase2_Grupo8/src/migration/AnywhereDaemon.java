package migration;

import java.util.ArrayList;

public class AnywhereDaemon {
	
	private static final String DATABASE_NAME = "test_database";
	private static final String USERNAME = "dba";
	private static final String PASSWORD = "sql";
	
	private static ConnectionHandler ch = new ConnectionHandler();
	private static DatabaseManager db = new DatabaseManager(DATABASE_NAME, USERNAME, PASSWORD);
	
	//TODO: All of the things that are to be inserted need to be popped from the stack and then sorted out.
	//This might work better on AnywhereDaemon?
	private String[] prepareValues(String values){
		ArrayList<String[]> newData = new ArrayList<String[]>();
		while (newData.isEmpty()) {
			newData = DataStack.popAll();
		}
		
		
		return null;
	}
	
	public static void main(String[] args) {
		db.insertStatement("TestTable", "Name, DateThing", "'test', '1985-1-6 21:55:10'", ch);
		System.out.println("Should be done!");
	}
}
