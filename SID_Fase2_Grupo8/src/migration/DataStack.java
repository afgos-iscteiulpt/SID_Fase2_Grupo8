package migration;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Used to store the data that is imported from MongoDB and later exported to SQLA.
 * @author pvmpa-iscteiulpt
 *
 */
public class DataStack {
	
	private static LinkedList<String[]> fifoMongoToSQLA = new LinkedList<String[]>();
	private static LinkedList<String[]> fifoSQLAToMongo = new LinkedList<String[]>();
	private static final String table = "HumidadeTemperatura";
	private static final String columns = "DataMedicao, HoraMedicao, ValorMedicaoTemperatura, ValorMedicaoHumidade, IDMedicao";
	private static final String[] datatypes = {"date", "time", "decimal", "decimal", "integer"};
	
	
	/////////////////////////////////
	//////fifoMongoToSQLA Stuff//////
	/////////////////////////////////
	
	/**
	 * Push data into the MongoToSQLA stack. Said data must be ORDERED DEPENDING ON THE TABLE YOU'RE MANIPULATING.\n
	 * Example: If you're manipulating a table 'Example(ID: int, Name: varchar, Date: timestamp)', then
	 * the pushed data should look something like this: {"1", "MyName", "2018-02-02 00:00:00'"}
	 * @param data
	 */
	public static void pushToSQLA(String[] data) {
		synchronized (fifoMongoToSQLA) {
			fifoMongoToSQLA.add(data);
			fifoMongoToSQLA.notifyAll();
		}
	}
	
	/**
	 * Pop data from the MongoToSQLA stack. Said data is a list of arrays of Strings. Each field of the array contains
	 * the data to be exported into SQLA.
	 * @return Stuff to put into the database
	 */
	public static ArrayList<String[]> popAllFromMongoToSQLA() {
		ArrayList<String[]> data = new ArrayList<String[]>();
		synchronized (fifoMongoToSQLA) {
			try {
				if (fifoMongoToSQLA.isEmpty()) {
					fifoMongoToSQLA.wait();
				}
				while (!fifoMongoToSQLA.isEmpty()) {
					data.add(fifoMongoToSQLA.removeFirst());
				}
			} catch (InterruptedException e) {
				System.out.println("WARNING: INTERRUPTED ON POP, ABORTING PROCEDURE IMMEDIATELY!!!");
				e.printStackTrace();
			}
		}
		return data;
	}
	
	/////////////////////////////////
	//////fifoSQLAToMongo Stuff//////
	/////////////////////////////////
	
	/**
	 * Pop data from the SQLAToMongo stack. Said data is a list of arrays of Strings. Each field of the array contains
	 * the data to be exported into SQLA.
	 * @return Stuff to put into the database
	 */
	public static ArrayList<String[]> popAllFromSQLAToMongo() {
		ArrayList<String[]> data = new ArrayList<String[]>();
		synchronized (fifoSQLAToMongo) {
			try {
				if (fifoSQLAToMongo.isEmpty()) {
					fifoSQLAToMongo.wait();
				}
				while (!fifoSQLAToMongo.isEmpty()) {
					data.add(fifoSQLAToMongo.removeFirst());
				}
			} catch (InterruptedException e) {
				System.out.println("WARNING: INTERRUPTED ON POP, ABORTING PROCEDURE IMMEDIATELY!!!");
				e.printStackTrace();
			}
		}
		return data;
	}
	
	/**
	 * Push data into the fifoSQLAToMongo stack. Said data must be ORDERED DEPENDING ON THE TABLE YOU'RE MANIPULATING.\n
	 * Example: If you're manipulating a table 'Example(ID: int, Name: varchar, Date: timestamp)', then
	 * the pushed data should look something like this: {"1", "MyName", "2018-02-02 00:00:00'"}
	 * Used for data consistency.
	 * @param data
	 */
	public static void pushToMongo(String[] data) {
		synchronized (fifoSQLAToMongo) {
			fifoSQLAToMongo.add(data);
			fifoSQLAToMongo.notifyAll();
		}
	}

	public static String getColumns() {
		return columns;
	}

	public static String getTable() {
		return table;
	}

	public static String[] getDatatypes() {
		return datatypes;
	}

}
