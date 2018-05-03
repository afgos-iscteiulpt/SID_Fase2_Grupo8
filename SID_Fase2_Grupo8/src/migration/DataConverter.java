package migration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class DataConverter {

	public static String[] convertJsonToStringArray(String json) {
		JSONObject obj = new JSONObject(json);
		String[] data= new String[5];
		data[0] = getDateFromEPOCH(obj.getJSONObject("DataHora").getInt("$date"));
		data[1] = getHourFromEPOCH(obj.getJSONObject("DataHora").getInt("$date"));
		data[2] = String.valueOf(obj.getDouble("ValorMedicaoTemperatura"));
		data[3] = String.valueOf(obj.getDouble("ValorMedicaoHumidade"));
		data[4] = String.valueOf(obj.getInt("IDMedicao"));
		return data;
	}
	
	public static String getDateFromEPOCH(int epoch) {
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		return format.format(new Date(Long.parseLong(String.valueOf(epoch))));
	}

	public static String getHourFromEPOCH(int epoch) {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(new Date(Long.parseLong(String.valueOf(epoch))));
	}
}
