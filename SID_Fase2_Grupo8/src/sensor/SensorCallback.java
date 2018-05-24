package sensor;

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
		BasicDBObject document = new BasicDBObject();
//		document.put("humidade", jobj.get("humidity"));
//		document.put("temperatura", jobj.get("temperature"));
//		document.put("date", jobj.get("date"));
//		document.put("time", jobj.get("time"));
//		System.out.println(document.toString());
		collection.insertOne(new Document().append("date", jobj.get("date")).append("time", jobj.get("time"))
				.append("temperatura", jobj.get("temperature")).append("humidade", jobj.get("humidity")));
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
	}

}
