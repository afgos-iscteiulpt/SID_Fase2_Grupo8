package migration;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Used to store the data that is imported from MongoDB and later exported to SQLA.
 * @author pvmpa-iscteiulpt
 *
 */
public class DataStack {
	
	private static LinkedList<String[]> fifo = new LinkedList<String[]>();
	
	/**
	 * Push data into the stack. Said data must be ORDERED DEPENDING ON THE TABLE YOU'RE MANIPULATING.\n
	 * Example: If you're manipulating a table 'Example(ID: int, Name: varchar, Date: timestamp)', then
	 * the pushed data should look something like this: {"1", "MyName", "2018-02-02 00:00:00'"}
	 * @param data
	 */
	public static void push(String[] data) {
		fifo.add(data);
	}
	
	public static ArrayList<String[]> popAll() {
		ArrayList<String[]> data = new ArrayList<String[]>();
		while (!fifo.isEmpty()) {
			data.add(fifo.removeFirst());
		}
		return data;
	}
}
