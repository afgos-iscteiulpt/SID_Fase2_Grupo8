package sensor;

import java.util.Calendar;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCollection;

public class SensorCallback implements MqttCallback {
	
	private MongoCollection<Document> collection;
	
	public SensorCallback( MongoCollection<Document> collection2) {
		this.collection = collection2;
	}
	
	
	public void connectionLost(Throwable cause) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		System.out.println("Message Arrived!");
		JSONObject jobj = null;
		// need to check if it's a JSON object at all
		try {
			jobj = new JSONObject(message.toString());
			//now check if the data is correct
			try {
				int year = Integer.parseInt(((String) jobj.get("date")).split("/")[2]);
				if (year > Calendar.getInstance().get(Calendar.YEAR))
					throw new IllegalArgumentException("Invalid year!");
				
				Double.parseDouble(((String) jobj.get("temperature")));
				Double.parseDouble(((String) jobj.get("humidity")));
				collection.insertOne(new Document().append("date", jobj.get("date")).append("time", jobj.get("time"))
						.append("temperatura", jobj.get("temperature")).append("humidade", jobj.get("humidity")));
				
			} catch (Exception e) {
				System.out.println(e);
			}
		} catch (JSONException e) {
			System.out.println("Invalid message: Detected non-JSON message in broker?");
		}

	}

	public void deliveryComplete(IMqttDeliveryToken token) {
	}

}
