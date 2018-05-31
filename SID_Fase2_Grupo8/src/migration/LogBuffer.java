package migration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class LogBuffer {

	public static void writeWarning(String text) {
		FileWriter fw;
		try {
			fw = new FileWriter("migration.log", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("[WARNING][" + Calendar.getInstance().getTime() + "] " + text + "\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void writeError(String text) {
		FileWriter fw;
		try {
			fw = new FileWriter("migration.log", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("[ERROR][" + Calendar.getInstance().getTime() + "] " + text + "\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
