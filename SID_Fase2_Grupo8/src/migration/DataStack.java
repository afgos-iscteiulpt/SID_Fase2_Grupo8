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
	private static final String columns = "Name, DateThing";
	
	/**
	 * Push data into the stack. Said data must be ORDERED DEPENDING ON THE TABLE YOU'RE MANIPULATING.\n
	 * Example: If you're manipulating a table 'Example(ID: int, Name: varchar, Date: timestamp)', then
	 * the pushed data should look something like this: {"1", "MyName", "2018-02-02 00:00:00'"}
	 * @param data
	 */
	public static void push(String[] data) {
		synchronized (fifo) {
			fifo.add(data);
			fifo.notifyAll();
		}
	}
	
	/**
	 * Pop data from the stack. Said data is a list of arrays of Strings. Each field of the array contains
	 * the data to be exported into SQLA.
	 * @return Stuff to put into the database
	 */
	public static ArrayList<String[]> popAll() {
		ArrayList<String[]> data = new ArrayList<String[]>();
		synchronized (fifo) {
			try {
				if (fifo.isEmpty()) {
					fifo.wait();
				}
				while (!fifo.isEmpty()) {
					data.add(fifo.removeFirst());
				}
			} catch (InterruptedException e) {
				System.out.println("WARNING: INTERRUPTED ON POP, ABORTING PROCEDURE IMMEDIATELY!!!");
				e.printStackTrace();
			}
		}
		return data;
	}

	public static String getColumns() {
		return columns;
	}

}
