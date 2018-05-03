package migration.SQLA;

import java.time.LocalDateTime;
import java.util.ArrayList;

import migration.DataStack;

public class AnywhereDaemon {
	
	private static final String DATABASE_NAME = "test_database";
	private static final String USERNAME = "dba";
	private static final String PASSWORD = "sql";
	
	private static ConnectionHandler ch = new ConnectionHandler();
	private static DatabaseManager db = new DatabaseManager(DATABASE_NAME, USERNAME, PASSWORD);
	
	private static ArrayList<String[]> prepareValues(){
		ArrayList<String[]> newData = new ArrayList<String[]>();
		while (newData.isEmpty()) {
			newData = DataStack.popAll();
		}
		return newData;
	}
	
	private static void insertValuesToDB() {
		ArrayList<String[]> data = prepareValues();
		String columns = DataStack.getColumns();
		String table = DataStack.getTable();
		
		for (String[] values: data) {
			String values_statement = "";
			
			for (int i = 0; i < values.length; i++) {
				switch (DataStack.getDatatypes()[i]) {
					case "integer":
						values_statement = values_statement.concat(values[i] + ",");
						break;
					case "varchar":
						values_statement = values_statement.concat("'" + values[i] + "',");
						break;
					case "decimal":
						values_statement = values_statement.concat(values[i] + ",");
						break;
					case "date":
						values_statement = values_statement.concat("'" + values[i] + "',");
						break;
					case "time":
						values_statement = values_statement.concat("'" + values[i] + "',");
						break;
					case "datetime":
						values_statement = values_statement.concat("'" + values[i] + "',");
						break;
					case "timestamp":
						values_statement = values_statement.concat("'" + values[i] + "',");
						break;
				}
			}
			values_statement = values_statement.substring(0, values_statement.length() - 1) + "";
			db.insertStatement(table, columns, values_statement, ch);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("SQLA-DAEMON: Beginning process...");
		while (true) {
			while (!db.isCloseDatabase()) {
				System.out.println("SQLA-DAEMON: Waiting for data on the stack...");
				insertValuesToDB();
				System.out.println("SQLA-DAEMON: Inserted new rows at " + LocalDateTime.now());
			}
			System.out.println("SQLA-DAEMON: WARNING - Database connection failed, attempting to reconnect...");
			db = new DatabaseManager(DATABASE_NAME, USERNAME, PASSWORD);
		}

	}
}
