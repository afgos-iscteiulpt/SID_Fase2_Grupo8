package sensor;

import static migration.DataConfig.*;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import migration.DataConfig;

public class MqttListener {
	
	private static final Integer PORT = 27017;


	public static void run() throws UnknownHostException, MqttException {
		
		new DataConfig().readProperties();
		String broker = "tcp://"+BROKER_URL+":1883";
		String topic = BROKER_TOPIC;
		String IPADDR = MONGO_URI;
		String clientId = "Listener1";
		
		//----------MONGO DB--------------
		System.out.println("Setting up credentials...");
		MongoCredential credential = MongoCredential.createCredential(MONGO_USERNAME, MONGO_AUTH_DB,
				MONGO_PASSWORD.toCharArray());
		System.out.println("Setting up settings...");
		MongoClientSettings settings = MongoClientSettings.builder().credential(credential)
				.applyToSslSettings(builder -> builder.enabled(false))
				.applyToClusterSettings(builder -> builder.hosts(Arrays.asList(new ServerAddress(IPADDR, PORT))))
				.build();
		
		System.out.println("Creating Mongo Client...");
		MongoClient mongo = MongoClients.create(settings);

		System.out.println("Grabbing database...");
		MongoDatabase db = mongo.getDatabase(MONGO_DBNAME);

		System.out.println("Grabbing collection...");
		MongoCollection<Document> collection = db.getCollection(SENSOR_COLLECTION_NAME);

		//----------MONGO DB--------------
		
		//----------MQTT CLIENT-----------
		SensorCallback cb = new SensorCallback(collection);
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
		
		//----------MQTT CLIENT-----------
	}

	public static void main(String[] args) throws UnknownHostException, MqttException {
		System.out.println("Mqtt Listener starting up...");
		MqttListener.run();

	}
}
