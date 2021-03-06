package migration.SQLA;

import java.time.LocalDateTime;
import java.util.ArrayList;

import migration.DataStack;
import static migration.DataConfig.*;

public class AnywhereDaemon {
	
	private static final String table = "HumidadeTemperatura";
	private static final String columns = "DataMedicao, HoraMedicao, ValorMedicaoTemperatura, ValorMedicaoHumidade";
	private static final String[] datatypes = { "date", "time", "decimal", "decimal" };
	
	private static ConnectionHandler ch = new ConnectionHandler();
	private static DatabaseManager db = new DatabaseManager(SQLA_DBNAME, SQLA_USERNAME, SQLA_PASSWORD);
	
	private static ArrayList<String[]> prepareValues(){
		ArrayList<String[]> newData = new ArrayList<String[]>();
		while (newData.isEmpty()) {
			newData = DataStack.popAllFromMongoToSQLA();
		}
		return newData;
	}
	
	private static void insertValuesToDB() {
		ArrayList<String[]> data = prepareValues();
		
		for (String[] values: data) {
			boolean successful_insert = false;
			String values_statement = parseValues(values);
			
			while (!successful_insert) {
				successful_insert = db.insertStatement(table, columns, values_statement, ch);
				if (successful_insert) {
					DataStack.pushToMongo(values);
				} else {
					System.out.println("WARNING: Insert failed, trying again in 1 sec...");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("Why am I being interrupted during my beauty sleep? " + e);
					}
					continue;
				}
			}
		}
	}

	private static String parseValues(String[] values) {
		String values_statement = "";
		for (int i = 0; i < values.length - 1; i++) { // values.length-1 because we don't want to export ObjectID to
														// SQLA
			switch (datatypes[i]) {
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
		return values_statement;
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
			db = new DatabaseManager(SQLA_DBNAME, SQLA_USERNAME, SQLA_PASSWORD);
		}

	}
}
