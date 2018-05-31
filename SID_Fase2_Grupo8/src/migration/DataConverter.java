package migration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class DataConverter {

	private static int maxVariationFlowPerSecond = 5;
	private static int lastTemperatura = -1000;
	private static int lastHumidade = -1000;
	private static long lastDate = -1000;

	public static String[] convertJsonToStringArray(String json) {
		JSONObject obj = new JSONObject(json);
		String[] data = new String[5];
		data[0] = convertDate(obj.getString("date"));
		data[1] = obj.getString("time");
		data[2] = obj.getString("temperatura");
		data[3] = obj.getString("humidade");
		data[4] = obj.getJSONObject("_id").getString("$oid");
		if (dataCheck(data)) {
			if (!isHighVariation(data))
				return data;
		}
		return null;
	}

	private static boolean dataCheck(String[] data) {
		// date was already checked in convert date
		return checkDate(data[0]) && checkTime(data[1]) && checkTemperatura(data[2]) && checkHumidade(data[3]) && data[4] != null;

	}

	private static boolean checkHumidade(String string) {
		int humid = Integer.parseInt(string);
		if (string != null &&  humid >= 0 && humid < 100) {
			return true;
		}
		LogBuffer.writeWarning("Humidity '" + string + "' was not valid");
		return false;
	}

	private static boolean checkTemperatura(String string) {
		if (string != null && Integer.parseInt(string) >= -272) {
			return true;
		}
		LogBuffer.writeWarning("Temperature '" + string + "' was not valid");
		return false;
	}

	private static boolean checkTime(String string) {
		try {
			DateFormat format = new SimpleDateFormat("HH:mm:ss");
			format.setLenient(false);
			@SuppressWarnings("unused") // if it's an invalid time, the parse will throw an exception
			Date theDate = format.parse(string);
		} catch (ParseException e) {
			LogBuffer.writeWarning("Time '" + string + "' was not valid");
			return false;
		}
		return true;
	}
	
	private static boolean checkDate(String string) {
		return string!=null;
	}

	private static boolean isHighVariation(String[] data) {
		if (lastTemperatura == -1000 || lastHumidade == -1000 || lastDate == -1000) {
			lastTemperatura = Integer.parseInt(data[2]);
			lastHumidade = Integer.parseInt(data[3]);
			lastDate = convertStringToEpoch(data[0], data[1]);
			return false;
		} else {
			long timeDifferenceInSeconds = (convertStringToEpoch(data[0], data[1]) - lastDate) / 1000;
			double temperaturaDifference = Integer.parseInt(data[2]) - lastTemperatura;
			double humidadeDifference = Integer.parseInt(data[3]) - lastHumidade;
			if (temperaturaDifference / timeDifferenceInSeconds > maxVariationFlowPerSecond
					|| humidadeDifference / timeDifferenceInSeconds > maxVariationFlowPerSecond) {
				LogBuffer.writeWarning("High Variation - Last Values     - Time:" + convertEpochToString(lastDate) + ", Temperature: " + lastTemperatura + ", Humidity: " + lastHumidade);
				LogBuffer.writeWarning("High Variation - Values Detected - Time:" + data[0] + " " + data[1] + ", Temperature: " + Integer.parseInt(data[2]) + ", Humidity: " + Integer.parseInt(data[3]));
				return true;
			}
			lastTemperatura = Integer.parseInt(data[2]);
			lastHumidade = Integer.parseInt(data[3]);
			lastDate = convertStringToEpoch(data[0], data[1]);
			return false;
		}
	}
	
	private static String convertEpochToString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(time));
	}

	private static long convertStringToEpoch(String date, String time) {
		try {
			String str = date + time;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
			Date temDate;
			temDate = df.parse(str);
			return temDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1000;
		}
	}

	public static String convertDate(String date) {
		Date initDate;
		try {
			initDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			return format.format(initDate);
		} catch (ParseException e) {
			LogBuffer.writeWarning("Date '" + date + "' was not valid");
			return null;
		}
	}

}
