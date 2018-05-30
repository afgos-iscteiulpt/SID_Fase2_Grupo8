package sensor;

import static migration.DataConfig.MONGO_DBNAME;
import static migration.DataConfig.MONGO_PASSWORD;
import static migration.DataConfig.MONGO_USERNAME;
import static migration.DataConfig.SENSOR_COLLECTION_NAME;

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
import static com.mongodb.client.model.Filters.*;

import migration.DataConfig;

public class MqttListener {
	
	private static final String IPADDR = "localhost";
	private static final Integer PORT = 27017;


	public static void run(String broker, String clientId, String topic) throws UnknownHostException, MqttException {
		
		new DataConfig().readProperties();
		//----------MONGO DB--------------
		System.out.println("Setting up credentials...");
		MongoCredential credential = MongoCredential.createCredential(MONGO_USERNAME, "admin",
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
		String BROKER_URL = "tcp://iot.eclipse.org:1883";
		String myTopic = "/sid_lab_2018";
		String clientID = "Listener1";
		MqttListener.run(BROKER_URL, clientID, myTopic);
	}
}
