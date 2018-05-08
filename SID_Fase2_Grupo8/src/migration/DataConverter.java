package migration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class DataConverter {

	public static String[] convertJsonToStringArray(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String[] data = new String[5];
			data[0] = convertDate(obj.getString("date"));
			data[1] = obj.getString("time");
			data[2] = String.valueOf(obj.getDouble("temperatura"));
			data[3] = String.valueOf(obj.getDouble("humidade"));
			return data;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String convertDate(String date) throws ParseException {
		Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(initDate);
	}
}
