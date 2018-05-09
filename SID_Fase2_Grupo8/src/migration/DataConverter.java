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
			data[2] = obj.getString("temperatura");
			data[3] = obj.getString("humidade");
			data[4] = obj.getJSONObject("_id").getString("$oid");
			if (dataCheck(data))
				return data;
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static boolean dataCheck(String[] data) throws ParseException {
		//date was already checked in convert date
		return 	checkTime(data[1]) &&
		checkTemperatura(data[2]) &&
		checkHumidade(data[3]) &&
		data[4]!=null;
		
	}

	private static boolean checkHumidade(String string) {
		if(string!=null) {
				int temp = Integer.parseInt(string);
				return temp>=0 && temp<100;
		}
		return false;
	}

	private static boolean checkTemperatura(String string) {
		if(string!=null) {
			int temp = Integer.parseInt(string);
			return temp>=-272;
		}
		return false;
	}

	private static boolean checkTime(String string) throws ParseException {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setLenient(false);
		@SuppressWarnings("unused") //if it's an invalid date, the parse will throw an exception
		Date theDate = format.parse(string);
		return true;
	}

	public static String convertDate(String date) throws ParseException {
		Date initDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setLenient(false);
		return format.format(initDate);
	}
	
	
}
