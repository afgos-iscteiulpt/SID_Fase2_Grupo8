package sensor;

import java.net.UnknownHostException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MqttListener {
	
	private static final String IPADDR = "localhost";
	private static final Integer PORT = 27017;

	public static void run(String broker, String clientId, String topic) throws UnknownHostException, MqttException {
		SensorCallback cb;
		MongoClient mongo = new MongoClient(IPADDR, PORT);
		DB db = mongo.getDB("sid");
		DBCollection collection = db.getCollection("sid");
		cb = new SensorCallback(collection);
		MemoryPersistence persistence = new MemoryPersistence();
		MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setAutomaticReconnect(true);
		connOpts.setKeepAliveInterval(10);
		sampleClient.setCallback(cb);
		sampleClient.connect(connOpts);
		sampleClient.subscribe(topic);
		System.out.println("Subscribed");
	}

	public static void main(String[] args) throws UnknownHostException, MqttException {
		String BROKER_URL = "tcp://iot.eclipse.org:1883";
		String myTopic = "SID2018-G8";
		String clientID = "Listener1";
		MqttListener.run(BROKER_URL, clientID, myTopic);
	}
}
