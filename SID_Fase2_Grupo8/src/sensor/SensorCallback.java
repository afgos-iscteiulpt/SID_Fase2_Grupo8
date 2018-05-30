package sensor;

import java.util.Calendar;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;

public class SensorCallback implements MqttCallback {
	
	private MongoCollection<Document> collection;
	
	public SensorCallback( MongoCollection<Document> collection2) {
		this.collection = collection2;
	}
	
	
	public void connectionLost(Throwable cause) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("Message Arrived!");
		JSONObject jobj = new JSONObject(message.toString());
			
		try {
			if(Integer.parseInt(((String) jobj.get("date")).split("/")[2]) == Calendar.YEAR)
			Double.parseDouble(((String) jobj.get("temperature")));
			Double.parseDouble(((String) jobj.get("humidity")));
			collection.insertOne(new Document().append("date", jobj.get("date")).append("time", jobj.get("time"))
			.append("temperatura", jobj.get("temperature")).append("humidade", jobj.get("humidity")));
			System.out.println("iok");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
	}

}
