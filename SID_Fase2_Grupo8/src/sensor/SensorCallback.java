package sensor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class SensorCallback implements MqttCallback {
	
	private DBCollection collection;
	
	public SensorCallback( DBCollection collection) {
		this.collection = collection;
	}
	
	
	public void connectionLost(Throwable cause) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		JSONObject jobj = new JSONObject(message.toString());
		BasicDBObject document = new BasicDBObject();
		document.put("humidade", jobj.get("humidity"));
		document.put("temperatura", jobj.get("temperature"));
		document.put("date", jobj.get("date"));
		document.put("time", jobj.get("time"));
		collection.insert(document);
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
	}

}
